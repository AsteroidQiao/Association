<template>
  <div>
    <!--头部-->
    <Header/>
    <!--主体-->
    <div>
      <div style="width: 70%; margin: 0 auto">
        <Main/>
      </div>
    </div>
  </div>

</template>

<script>


import Main from "@/views/front/main";
import Header from "@/views/front/header";

export default {
  name: "front",
  components: { Header, Main},
  data() {
    return {};
  },

  methods: {},
};
</script>

<style>
body {

}


</style>
