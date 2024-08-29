<template>
  <table style="border: 1px" v-if="postState.state">
    <tr>
      <td>작성자: {{ postState.view.postWriter }}</td>
      <td>등록일자: {{ postState.view.postCreatedDate }}</td>
      <td>수정일자: {{ postState.view.postRevisionDate }}</td>
    </tr>
    <tr>
      <td colspan="2">
        [{{ postState.view.categoryName }}]
        {{ postState.view.postTitle }}
      </td>
      <td>조회수: {{ postState.view.postViews }}</td>
    </tr>
    <tr>
      <td class="content" colspan="3">
        {{ postState.view.postContent }}
      </td>
    </tr>
    <tr class="files">
      <td v-for="file in postState.files">
        <svg xmlns="http://www.w3.org/2000/svg" id="Outline" viewBox="0 0 24 24" width="12" height="12">
          <path d="M9.878,18.122a3,3,0,0,0,4.244,0l3.211-3.211A1,1,0,0,0,15.919,13.5l-2.926,2.927L13,1a1,1,0,0,0-1-1h0a1,1,0,0,0-1,1l-.009,15.408L8.081,13.5a1,1,0,0,0-1.414,1.415Z"/>
          <path d="M23,16h0a1,1,0,0,0-1,1v4a1,1,0,0,1-1,1H3a1,1,0,0,1-1-1V17a1,1,0,0,0-1-1H1a1,1,0,0,0-1,1v4a3,3,0,0,0,3,3H21a3,3,0,0,0,3-3V17A1,1,0,0,0,23,16Z"/>
        </svg>
        <a :href=postState.downloadLink+file.fileId>{{ file.fileName }}</a>
      </td>
    </tr>
    <comment :post-state/>
    <tr>
      <td>
        <button type="button" @click="goToList">목록</button>
      </td>
      <td>
        <button type="button" @click="modifyPost(postState.postId)">수정</button>
      </td>
      <td>
        <button type="button" @click="removePost">삭제</button>
      </td>
    </tr>
  </table>
  <dialog ref="dialog">
    <article>
      <h2>게시글 삭제</h2>
      <input type="password" placeholder="비밀번호를 입력해 주세요." v-model="postState.password">
      <footer>
        <button @click="removeCancel" class="secondary">취소</button>
        <button @click="removeConfirm">확인</button>
      </footer>
    </article>
  </dialog>
</template>

<script setup>
import {instance} from '@/modules/axios';
import {useRoute, useRouter} from 'vue-router';
import {onMounted, reactive, ref} from 'vue';
import comment from '@/components/comment.vue'

const router = useRouter();
const route = useRoute();

const dialog = ref();
const postState = reactive({
  postId: 0,
  commentWriter: "",
  commentContent: "",
  password: "",
  comments: [],
  files: [],
  view: {},
  downloadLink: "http://localhost:8080/api/view/download?fileId=",
  state: false
});

onMounted(() => {
  postState.postId = parseInt(sessionStorage.getItem("postId"));
  getData();
});

const getData = async () => {
  try {
    const params = {params: {postId: postState.postId}};

    const response = await instance.get("/view", params);

    dataSetUp(response.data.object);
  } catch (error) {
    await router.push({name: "List", query: route.query});
  }
};

const dataSetUp = (data) => {
  postState.state = true;
  postState.view = data.view;
  postState.comments = data.comment;
  postState.files = data.files;
}

const modifyPost = (postId) => {
  sessionStorage.setItem("postId", postId);

  router.push({name: "Modify", query: route.query});
};

const removePost = () => {
  dialog.value.showModal();
};

const removeCancel = () => {
  dialog.value.close();
};

const removeConfirm = async () => {
  try {
    const params = {params: {postId: postState.postId, postPassword: postState.password}};

    const response = await instance.delete("/view/remove-post", params);

    alert(response.data.message);

    await router.push({name: "List", query: route.query});
  } catch (error) {

  }
}

const goToList = () => {
  router.push({name: "List", query: route.query});
};
</script>

<style scoped>
.files {
  display: flex;
  flex-direction: column;
}

.files td {
  border-bottom: 1px solid #ddd;
  padding: 8px;
}
</style>