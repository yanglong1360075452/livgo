<template>
    <!--首页-->
    <div class="view-wrap"  v-loading="loading" element-loading-text="拼命加载中~~~">
        <my-header></my-header>
        <my-menu></my-menu>
        <router-view></router-view>
      <!--</div>-->
    </div>
    <!--/首页-->
</template>
<script>
    import MyHeader from '../../components/head.vue';
    import MyMenu from '../../components/menu.vue';
    import {mapState} from 'vuex'
    export default {
        name: 'index',
        components: {
            MyHeader,
            MyMenu
        },
      computed: mapState([
        // 映射 this.loading 为 store.state.loading
        'loading'
      ])
    }
</script>
<style lang="scss">
    @import "../../assets/scss/define";
    .index-view{
        @extend %pr;
    }
    .menu-wrap{
        @extend %h100;
        width: 200px;
    }
    .container-wrap{
        @extend %pa;
        @extend %oh;
        top: 60px;
        left: 200px;
        right: 0;
        bottom: 0;
    }
</style>
