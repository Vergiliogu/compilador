export interface ElectronAPI {
  openFileDialog: () => Promise<string | null>;
  openSaveFileDialog: (content: string) => Promise<{ success: boolean, filePath: string, error?: any }>;
  readFile: (filePath: string) => Promise<string | null>;
  writeFile: (filePath: string, content: string) => Promise<{ success: boolean, filePath: string, error?: any }>;
}

declare global {
  interface Window {
    electron: ElectronAPI;
  }
}