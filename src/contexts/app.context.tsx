import { createContext, PropsWithChildren, useState } from "react";

interface AppContextType {
  status?: string;
  terminalHeight: number;
  setTerminalHeight: (height: number) => void;
}

export const AppContext = createContext<AppContextType | undefined>(undefined);

export const AppContextProvider = ({ children }: PropsWithChildren) => {
  const [terminalHeight, setTerminalHeight] = useState(100);

  return (
    <AppContext.Provider value={{
      status: undefined,
      terminalHeight,
      setTerminalHeight,
    }}>
      {children}
    </AppContext.Provider>
  );
}
