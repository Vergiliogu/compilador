import { Editor } from "./components/Editor"
import { MessageTerminal } from "./components/MessageTerminal"
import { StatusBar } from "./components/StatusBar"
import { ToolsBar } from "./components/ToolsBar"
import { AppContextProvider } from "./contexts/app.context"

function App() {

  return (
    <AppContextProvider>
      <div className="w-full h-screen flex flex-col">
        <ToolsBar />
        <Editor />
        <MessageTerminal />
        <StatusBar />
      </div>
    </AppContextProvider>
  )
}

export default App
