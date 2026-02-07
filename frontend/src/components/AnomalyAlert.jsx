import { useEffect, useState } from "react";
import axios from "axios";

export default function AnomalyAlert() {
  const [alert, setAlert] = useState("");

  useEffect(() => {
    const checkAnomaly = () => {
      axios
        .get("http://localhost:8080/api/logs/anomaly/order-service")
        .then((res) => {
          if (res.data.includes("SPIKE")) {
            setAlert(res.data);
          } else {
            setAlert("");
          }
        });
    };

    checkAnomaly();
    const interval = setInterval(checkAnomaly, 5000);

    return () => clearInterval(interval);
  }, []);

  if (!alert) return null;

  return (
    <div className="bg-red-500 text-white p-3 rounded-lg mb-4 shadow">
      🚨 {alert}
    </div>
  );
}
