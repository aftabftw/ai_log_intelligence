import { useEffect, useState } from "react";
import axios from "axios";
import {
  Chart as ChartJS,
  BarElement,
  CategoryScale,
  LinearScale,
  Tooltip,
  Legend,
} from "chart.js";
import { Bar } from "react-chartjs-2";

ChartJS.register(BarElement, CategoryScale, LinearScale, Tooltip, Legend);

export default function ErrorFrequencyChart() {
  const [chartData, setChartData] = useState({});

  useEffect(() => {
    axios
      .get("http://localhost:8080/api/logs/root-causes")
      .then((res) => {
        const labels = res.data.map((item) => item.groupId);
        const values = res.data.map((item) => item.frequency);

        setChartData({
          labels,
          datasets: [
            {
              label: "Error Frequency",
              data: values,
              backgroundColor: "rgba(59,130,246,0.6)",
            },
          ],
        });
      });
  }, []);

  return (
    <div className="bg-white p-4 rounded-xl shadow mt-6">
      <h2 className="text-xl font-semibold mb-3">
        Error Frequency Chart
      </h2>

      {chartData.labels && <Bar data={chartData} />}
    </div>
  );
}
