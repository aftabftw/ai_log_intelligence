import { useState } from "react";
import SockJS from "sockjs-client";
import Stomp from "stompjs";
import { useEffect } from "react";
import RootCauseTable from "../components/RootCauseTable";
import ExplanationPanel from "../components/ExplanationPanel";
import ErrorFrequencyChart from "../components/ErrorFrequencyChart";
import AnomalyAlert from "../components/AnomalyAlert";


export default function Dashboard() {
  const [selectedGroup, setSelectedGroup] = useState(null);
    useEffect(() => {
    const socket = new SockJS("http://localhost:8080/logs");
    const stompClient = Stomp.over(socket);

    stompClient.connect({}, () => {
    stompClient.subscribe("/topic/log-updates", (msg) => {
  console.log("Realtime Event Received:", msg.body);
  window.location.reload();
});

    });
    }, []);


  return (
    <div className="min-h-screen bg-gray-100 p-6">
      <h1 className="text-3xl font-bold mb-6">
        Log Intelligence Dashboard
      </h1>
      
      <AnomalyAlert />

      <div className="grid grid-cols-2 gap-6">
        <RootCauseTable onSelect={setSelectedGroup} />
        <ExplanationPanel groupId={selectedGroup} />
      </div>
      <ErrorFrequencyChart />

    </div>
  );
}
