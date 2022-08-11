<template>

  <!--  <el-button @click="resetDateFilter">reset date filter</el-button>
    <el-button @click="clearFilter">reset all filters</el-button>-->
  <el-form :inline="true" :model="queryParam" class="demo-form-inline">
    <el-form-item label="用户名：">
      <el-input v-model="queryParam.userName" placeholder="用户名" />
    </el-form-item>
    <el-form-item label="昵称：">
      <el-input v-model="queryParam.nickName" placeholder="昵称" />
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit">查询</el-button>
    </el-form-item>
  </el-form>
  <div class="flex">
    <el-button type="primary" :icon="Edit" />
    <el-button type="primary" :icon="Share" />
    <el-button type="primary" :icon="Delete" />
    <el-button type="primary" :icon="Search">Search</el-button>
    <el-button type="primary">
      Upload<el-icon class="el-icon--right"><Upload /></el-icon>
    </el-button>
  </div>
  <el-table ref="tableRef" row-key="date" :data="tableData" style="width: 100%">
    <el-table-column prop="userName" label="用户名"/>
    <el-table-column prop="nickName" label="昵称"/>
    <el-table-column prop="email" label="邮箱"/>
    <el-table-column prop="password" label="密码"/>
    <el-table-column prop="phonenumber" label="手机号"/>
    <el-table-column prop="createTime" label="创建时间"/>
    <el-table-column prop="loginIp" label="IP"/>
    <el-table-column prop="sex" label="性别"/>
  </el-table>
  <el-pagination background layout="prev, pager, next" :total="totalCount" :page-size="queryParam.size"
                 @current-change="changePage"/>
</template>

<script setup>

import {ref} from 'vue'
import {ElTable} from 'element-plus'
import {onMounted} from "vue";
import {listUser} from "@/api/user";
import { Delete, Edit, Search, Share, Upload } from '@element-plus/icons-vue'

const queryParam = ref({
  page: 0,
  size:2,
  userName:'',
  nickName:''

});

const tableData = ref();
const totalCount = ref(0);


const getList = function () {
  listUser(queryParam.value).then(res => {
    tableData.value = res.data.content
    totalCount.value = res.data.totalElements
    queryParam.value.size = res.data.pageable.pageSize
  })
}

onMounted(() => {
  getList();
})

const changePage = function (current) {
  queryParam.value.page = current-1;
  getList();
}

const onSubmit = function (){
  getList();
}


</script>
