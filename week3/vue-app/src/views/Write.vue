<template>
  <form ref="writeForm">
    <table style="border: 1px">
      <tr>
        <th>카테고리</th>
        <td>
          <select name="categoryId" v-model="postFormState.categoryId">
            <option value="">카테고리 선택</option>
            <option v-for="category in postFormState.categories"
                    v-bind:value="category.categoryId">
              {{ category.categoryName }}
            </option>
          </select>
        </td>
      </tr>
      <tr>
        <th>작성자</th>
        <td>
          <input name="postWriter" type="text" v-model="postFormState.postWriter">
        </td>
      </tr>
      <tr>
        <th>비밀번호*</th>
        <td>
          <input name="postPassword" type="password" placeholder="비밀번호" v-model="postFormState.postPassword">
          <input name="confirmPassword" type="password" placeholder="비밀번호 확인" v-model="postFormState.confirmPassword">
        </td>
      </tr>
      <tr>
        <th>제목*</th>
        <td>
          <input name="postTitle" type="text" v-model="postFormState.postTitle">
        </td>
      </tr>
      <tr>
        <th>내용*</th>
        <td>
          <textarea name="postContent" v-model="postFormState.postContent"></textarea>
        </td>
      </tr>
      <tr>
        <th>파일첨부*</th>
        <td>
          <input v-for="file in 3" v-bind:key="file" name="file" type="file">
        </td>
      </tr>
      <tr>
        <td colspan="2">
          <button type="button" @click="goToList">취소</button>
          <button type="button" @click="savePost">저장</button>
        </td>
      </tr>
    </table>
  </form>
</template>

<script setup>
import {instance} from '@/modules/axios';
import {writeValid} from '@/modules/validation'
import {useRoute, useRouter} from 'vue-router';
import {ref, reactive, onMounted} from 'vue';

const router = useRouter();
const route = useRoute();

const writeForm = ref();
const postFormState = reactive({
  categoryId: "",
  postWriter: "",
  postPassword: "",
  confirmPassword: "",
  postTitle: "",
  postContent: "",
  categories: []
});

onMounted(() => {
  getData();
});

const getData = async () => {
  try {
    const response = await instance.get("/write");

    postFormState.categories = response.data;
  } catch (error) {

  }
};

const savePost = async () => {
  try {
    if (!writeValid(postFormState)) return;

    const formData = new FormData(writeForm.value);

    const response = await instance.post("/write", formData);

    alert(response.data.message);

    await router.push({name: "List", query: {}});
  } catch (error) {

  }
};

const goToList = () => {
  router.push({name: "List", query: route.query});
};
</script>