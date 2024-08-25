export interface ElectronAPI {
  openFileDialog: () => Promise<string | null>;
  openSaveFileDialog: (content: string) => Promise<{ success: boolean, filePath: string }>;
  readFile: (filePath: string) => Promise<string | null>;
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