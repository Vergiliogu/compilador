import { app, BrowserWindow, ipcMain, dialog } from 'electron';
import { fileURLToPath } from 'node:url';
import path from 'node:path';
import { readFileSync, writeFileSync, accessSync, constants } from 'node:fs';

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
      writeFileSync(filePath, content, 'utf-8');
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
    writeFileSync(filePath, content, 'utf-8');
    return { success: true, filePath };
  } catch (error) {
    console.error('Error saving file:', error);
    return { success: false, error };
  }
});

ipcMain.handle('read-file', async (_event: unknown, filePath: string) => {
  try {
    const data = readFileSync(filePath, 'utf-8'); // Read file content
    return data; // Return file content
  } catch (error: any) {
    console.error('Error reading file:', error);
    return null; // Return null in case of an error
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
