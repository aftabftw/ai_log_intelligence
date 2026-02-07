import { useEffect, useState } from "react";
import axios from "axios";

export default function ExplanationPanel({ groupId }) {
  const [text, setText] = useState("");

  useEffect(() => {
    if (!groupId) return;

    setText("Loading AI explanation...");

    axios
      .get(`http://localhost:8080/api/logs/explain/${groupId}`)
      .then((res) => setText(res.data))
      .catch(() => setText("Failed to load explanation"));
  }, [groupId]);

  return (
    <div className="bg-white p-4 rounded-xl shadow">
      <h2 className="text-xl font-semibold mb-3">AI Explanation</h2>

      <pre className="whitespace-pre-wrap text-sm">
        {text}
      </pre>
    </div>
  );
}
