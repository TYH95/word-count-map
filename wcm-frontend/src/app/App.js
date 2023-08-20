import "./App.css";
import useWebSocket from "@components/useWebsocket";
import WordCountMapViewer from "@components/WordCountMapViewer";

function App() {
  const connectionStatus = useWebSocket('ws:127.0.0.1:8080/ws')
  console.log(connectionStatus)
  return (
    <div className="App">
      <WordCountMapViewer></WordCountMapViewer>
    </div>
  );
}

export default App;
