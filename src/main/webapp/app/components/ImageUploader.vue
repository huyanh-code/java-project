<!-- 

reference: https://github.com/azeemade/image-upload-vue/blob/main/src/components/single-upload/createSingle.vue 

-->

<template>
  <div>
    <!--Image attachment preview section-->
    <div class="text-center">
      <img
        :src="preview == '' ? default_img : preview"
        class="img-thumbnail custom-preview-style"
        :class="img_sc_style.border"
        :max-height="img_sc_style.height"
        :max-width="img_sc_style.width"
      />
    </div>

    <!--Image attachment upload section-->
    <div class="d-flex justify-content-center mt-1 align-items-baseline">
      <div>
        <label for="scfile" class="text-primary btn-sm btn mb-0" title="Upload image">
          <input type="file" id="scfile" name="scImage" autocomplete="off" @change="attachimage" class="hidden" />
          {{ img_sc_title }}
        </label>
      </div>
      <div v-show="del">
        <button class="btn text-danger px-0" @click.prevent="deleteImage">
          <font-awesome-icon icon="trash"></font-awesome-icon>
        </button>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'ImageUploader',
  props: {
    img_sc_style: {
      type: Object,
      default() {
        return {
          width: '240px',
          height: '240px',
          border: '',
        };
      },
    },
    img_sc_title: {
      type: String,
      default: 'Choose image',
    },
    default_img: {
      type: String,
      default: 'https://github.com/azeemade/image-upload-vue/blob/main/src/assets/default.png?raw=true',
    },
  },
  data() {
    return {
      preview: '',
      del: false,
    };
  },
  methods: {
    attachimage(e) {
      e.preventDefault();
      var files = e.target.files;

      if (!files.length) return;
      this.createImage(files[0]);

      this.del = true;
      // this.$store.commit('addCreateSingle', files[0])
      this.$emit('fileAdded', files[0]);
      return files;
    },
    createImage(file) {
      var reader = new FileReader();
      reader.onload = e => {
        this.preview = e.target.result;
      };

      reader.readAsDataURL(file);
    },
    deleteImage(files) {
      var file = Array.from(files);
      file.splice(0, 1);

      this.del = false;
      this.preview = '';
      this.$emit('fileRemoved');
    },
    tipMethod() {
      // Note this is called each time the tooltip is first opened.
      return '<strong>' + new Date() + '</strong>';
    },
  },
};
</script>
<style scoped>
.hidden {
  display: none;
}
.custom-preview-style {
  max-width: 240px !important;
  max-height: 240px !important;
}
</style>
