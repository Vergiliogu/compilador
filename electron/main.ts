import { app, BrowserWindow, ipcMain, dialog } from 'electron';
import { fileURLToPath } from 'node:url';
import path from 'node:path';
import { accessSync, constants } from 'node:fs';
import { writeFile, readFile } from 'node:fs/promises';
import { exec } from 'node:child_process';
import util from 'node:util';
import { tmpdir } from 'node:os';

const __dirname = path.dirname(fileURLToPath(import.meta.url));

process.env.APP_ROOT = path.join(__dirname, '..');

export const VITE_DEV_SERVER_URL = process.env['VITE_DEV_SERVER_URL'];
export const MAIN_DIST = path.join(process.env.APP_ROOT, 'dist-electron');
export const RENDERER_DIST = path.join(process.env.APP_ROOT, 'dist');

process.env.VITE_PUBLIC = VITE_DEV_SERVER_URL ? path.join(process.env.APP_ROOT, 'public') : RENDERER_DIST;

let win: BrowserWindow | null;

function createWindow() {
  win = new BrowserWindow({
    icon: path.join(process.env.VITE_PUBLIC, 'electron-vite.svg'),
    minWidth: 910,
    minHeight: 600,
    webPreferences: {
      preload: path.join(__dirname, 'preload.mjs'),
      devTools: false, // Disable DevTools
      contextIsolation: true, // Ensures security when exposing APIs
      nodeIntegration: false, // Ensures no direct access to Node APIs
    },
  });

  win.webContents.openDevTools();

  // Test active push message to Renderer-process.
  win.webContents.on('did-finish-load', () => {
    win?.webContents.send('main-process-message', new Date().toLocaleString());
  });

  if (VITE_DEV_SERVER_URL) {
    win.loadURL(VITE_DEV_SERVER_URL);
  } else {
    win.loadFile(path.join(RENDERER_DIST, 'index.html'));
  }
}

// Handle file dialog request from renderer
ipcMain.handle('open-file-dialog', async () => {
  const result = await dialog.showOpenDialog(win!, {
    properties: ['openFile'],
    filters: [{ name: 'Text Files', extensions: ['txt'] }]
  });
  return result.filePaths;
});

ipcMain.handle('save-file-dialog', async (_event, content: string) => {
  const { filePath } = await dialog.showSaveDialog(win!, {
    title: 'Salvar arquivo',
    filters: [
      { name: 'Text Files', extensions: ['txt'] },
      { name: 'All Files', extensions: ['*'] },
    ],
  });

  if (filePath) {
    try {
      await writeFile(filePath, content, 'utf-8');
      return { success: true, filePath };
    } catch (error) {
      console.error('Error saving file:', error);
      return { success: false, error };
    }
  }

  return { success: false };
});

ipcMain.handle('write-file', async (_event, filePath: string, content: string) => {
  try {
    accessSync(filePath, constants.W_OK);
    await writeFile(filePath, content, 'utf-8');
    return { success: true, filePath };
  } catch (error) {
    console.error('Error saving file:', error);
    return { success: false, error };
  }
});

ipcMain.handle('read-file', async (_event: unknown, filePath: string) => {
  try {
    const data = await readFile(filePath, 'utf-8');
    return data; // Return file content
  } catch (error: any) {
    console.error('Error reading file:', error);
    return null; // Return null in case of an error
  }
});

ipcMain.handle('write-lexical-file', async (_event, content: string) => {
  const filePath = path.join(tmpdir(), 'source-code.txt');

  try {
    await writeFile(filePath, content, 'utf-8');
    return { success: true, filePath };
  } catch (error) {
    console.error('Error saving file:', error);
    return { success: false, error };
  }
});

const execPromise = util.promisify(exec);

ipcMain.handle('run-lexical', async () => {
  let appPath: string;
  if (process.env.NODE_ENV === 'development')
    appPath = process.env.APP_ROOT
  else {
    appPath = path.join(app.getAppPath(), '..')
  }

  const sourceCodeFile = path.join(tmpdir(), 'source-code.txt');
  const classPath = `-cp ${path.join(appPath, 'domain', 'lexical', 'bin', 'classes')}`
  const className = 'com.domain.compiler.App'

  try {
    const { stderr, stdout } = await execPromise(
      `java ${classPath} ${className} ${sourceCodeFile}`,
      { encoding: 'latin1' }
    );

    if (stderr) {
      console.error('Error running lexical:', stderr);
      return { success: false, error: stderr };
    }

    return { success: true, output: stdout };
  } catch (error) {
    console.error('Error running lexical:', error);
    return { success: false, error };
  }
});

// Quit when all windows are closed, except on macOS.
app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') {
    app.quit();
    win = null;
  }
});

app.on('activate', () => {
  if (BrowserWindow.getAllWindows().length === 0) {
    createWindow();
  }
});

app.whenReady().then(createWindow);
