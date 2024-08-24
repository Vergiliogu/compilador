import { createContext, PropsWithChildren } from "react";

interface AppContextType {
  status?: string;
}

export const AppContext = createContext<AppContextType | undefined>(undefined);

export const AppContextProvider = ({ children }: PropsWithChildren) => {
  return (
    <AppContext.Provider value={{ status: undefined }}>
      {children}
    </AppContext.Provider>
  );
}
