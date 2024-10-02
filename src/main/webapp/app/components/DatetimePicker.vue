<template>
  <div>
    <span>test</span>
    <!-- <Datepicker v-model="date" auto-apply format="dd/MM/yyyy" /> -->

    <VueDatePicker v-model="date" auto-apply format="dd/MM/yyyy" />
    <p v-if="formattedDate">Bạn đã chọn: {{ formattedDate }}</p>
  </div>
</template>

<script>
export default {
  compatConfig: {
    MODE: 2,
    // COMPONENT_V_MODEL: false,
  },
};
</script>

<script setup>
import { ref, watch } from 'vue';
// import Datepicker from '@vuepic/vue-datepicker';
// import '@vuepic/vue-datepicker/dist/main.css';

// Khai báo biến date với giá trị mặc định là null
const date = ref();
const formattedDate = ref(''); // Khai báo biến để lưu trữ ngày đã định dạng

// Theo dõi thay đổi trên biến date
// watch(date, (newValue) => {
//     if (newValue) {
//         // Lấy ngày, tháng, năm từ đối tượng Date
//         const day = newValue.getDate().toString().padStart(2, '0');
//         const month = (newValue.getMonth() + 1).toString().padStart(2, '0'); // Tháng bắt đầu từ 0
//         const year = newValue.getFullYear();

//         // Định dạng lại thành DD/MM/YYYY
//         formattedDate.value = `${day}/${month}/${year}`;
//     } else {
//         formattedDate.value = ''; // Đặt lại nếu không có ngày nào được chọn
//     }
// });
</script>
