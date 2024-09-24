<script>
import { Bar } from 'vue-chartjs';
import { Chart as ChartJS, Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale } from 'chart.js';

ChartJS.register(Title, Tooltip, Legend, BarElement, CategoryScale, LinearScale);

export default {
  name: 'BarChart',
  components: { Bar },
  data() {
    return {
      chartData: {
        labels: ['January', 'February', 'March', 'January', 'February', 'March', 'aaaa'],
        datasets: [
          {
            label: 'My First Dataset',
            data: [65, 59, 80, 81, 56, 55, 40],
            backgroundColor: [
              'rgba(255, 99, 132, 0.2)',
              'rgba(255, 159, 64, 0.2)',
              'rgba(255, 205, 86, 0.2)',
              'rgba(75, 192, 192, 0.2)',
              'rgba(54, 162, 235, 0.2)',
              'rgba(153, 102, 255, 0.2)',
              'rgba(201, 203, 207, 0.2)',
            ],
            borderColor: [
              'rgb(255, 99, 132)',
              'rgb(255, 159, 64)',
              'rgb(255, 205, 86)',
              'rgb(75, 192, 192)',
              'rgb(54, 162, 235)',
              'rgb(153, 102, 255)',
              'rgb(201, 203, 207)',
            ],
            borderWidth: 1,
          },

          {
            label: 'My Second Dataset',
            data: [165, 159, 180, 8, 5, 55, 40],
            backgroundColor: [
              'rgba(255, 99, 132, 0.2)',
              'rgba(255, 159, 64, 0.2)',
              'rgba(255, 205, 86, 0.2)',
              'rgba(75, 192, 192, 0.2)',
              'rgba(54, 162, 235, 0.2)',
              'rgba(153, 102, 255, 0.2)',
              'rgba(201, 203, 207, 0.2)',
            ],
            borderColor: [
              'rgb(255, 99, 132)',
              'rgb(255, 159, 64)',
              'rgb(255, 205, 86)',
              'rgb(75, 192, 192)',
              'rgb(54, 162, 235)',
              'rgb(153, 102, 255)',
              'rgb(201, 203, 207)',
            ],
            borderWidth: 1,
          },
        ],
      },
      chartOptions: {
        responsive: true,
        scales: {
          y: {
            beginAtZero: true,
          },
        },
      },
    };
  },
};

// export default {
//   name: "bar-chart",
//   extends: Bar,
//   // mixins: [mixins.reactiveProp],
//   mixins: [],
//   props: {
//     extraOptions: Object,
//     gradientColors: {
//       type: Array,
//       default: () => [
//         "rgba(72,72,176,0.2)",
//         "rgba(72,72,176,0.0)",
//         "rgba(119,52,169,0)",
//       ],
//       validator: (val) => {
//         return val.length > 2;
//       },
//     },
//     gradientStops: {
//       type: Array,
//       default: () => [1, 0.4, 0],
//       validator: (val) => {
//         return val.length > 2;
//       },
//     },
//   },
//   data() {
//     return {
//       ctx: null,
//     };
//   },
//   methods: {
//     updateGradients(chartData) {
//       if (!chartData) return;
//       const ctx =
//         this.ctx || document.getElementById(this.chartId).getContext("2d");
//       const gradientStroke = ctx.createLinearGradient(0, 230, 0, 50);

//       gradientStroke.addColorStop(
//         this.gradientStops[0],
//         this.gradientColors[0]
//       );
//       gradientStroke.addColorStop(
//         this.gradientStops[1],
//         this.gradientColors[1]
//       );
//       gradientStroke.addColorStop(
//         this.gradientStops[2],
//         this.gradientColors[2]
//       );
//       chartData.datasets.forEach((set) => {
//         set.backgroundColor = gradientStroke;
//       });
//     },
//   },
//   mounted() {
//     this.$watch(
//       "chartData",
//       (newVal, oldVal) => {
//         this.updateGradients(newVal);
//         // if (!oldVal) {
//         //   this.renderChart(this.chartData, this.extraOptions);
//         // }
//       },
//       { immediate: true }
//     );
//   },
// };
</script>

<template>
  <Bar id="my-chart-id" :options="chartOptions" :data="chartData" />
</template>
