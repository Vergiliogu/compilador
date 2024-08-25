export interface ElectronAPI {
  openFileDialog: () => Promise<string | null>;
  openSaveFileDialog: (content: string) => Promise<{ success: boolean, filePath: string, error?: any }>;
  readFile: (filePath: string) => Promise<string | null>;
  writeFile: (filePath: string, content: string) => Promise<{ success: boolean, filePath: string, error?: any }>;
  clipboard: {
    writeText: (text: string) => void;
    readText: () => string;
  }
}

declare global {
  interface Window {
    electron: ElectronAPI;
  }
}