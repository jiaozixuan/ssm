<template>
  <router-view/>
<!--  <el-button type="primary">Primary</el-button>-->
</template>

<script>
export default {
  name: 'HelloWorld',
  mounted() {
    this.$router.push({name: 'login'})
  }
}
</script>

<style>

</style>