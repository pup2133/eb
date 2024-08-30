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
        <button type="button" @click="goToModify">수정</button>
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
import {useRoute, useRouter} from 'vue-router';
import {onMounted, ref} from 'vue';
import comment from '@/components/comment.vue'
import boardService from "@/service/boardService";

const router = useRouter();
const route = useRoute();

/* <dialog> */
const dialog = ref();

/**
 * 1. commentWriter: 댓글 작성자
 * 2. commentContent: 댓글 내용
 * 3. password: 게시글 삭제 시 사용하는 패스워드
 * 4. comments: 게시글 댓글 목록
 * 5. files: 게시글 파일 목록
 * 6. view: 게시글 정보
 * 7. downloadLink: 파일 다운로드시 사용
 * 8. state: <table> 렌더링할 때 사용
 * @type {Ref<UnwrapRef<{password: string, view: {}, comments: *[], downloadLink: string, files: *[], commentWriter: string, commentContent: string, state: boolean}>>}
 */
const postState = ref({
  commentWriter: "",
  commentContent: "",
  password: "",
  comments: [],
  files: [],
  view: {},
  downloadLink: "http://localhost:8080/api/view/download?fileId=",
  state: false
});

/* 컴포넌트 마운트 된 후 실행 */
onMounted(() => {
  postState.value.view.postId = parseInt(sessionStorage.getItem("postId"));
  getView();
});

/**
 * 1. boardService.getView(postId) 호출하여 받아온 데이터로
 * 2. 가져온 데이터로 상태 변경
 * 3. 가져온 데이터가 없으면 게시글 목록으로 이동
 * @returns {Promise<void>}
 */
const getView = async () => {
  try {
    const response = await boardService.getView(postState.value.view.postId);
    viewDataSetUp(response.data.object)
  } catch (error) {
    await boardService.goToList(router, route);
  }
};

/**
 * 1. 서버에서 받아온 데이터로 postState 상태 변경
 * @param data
 */
const viewDataSetUp = (data) => {
  postState.value.state = true;
  postState.value.view = data.view;
  postState.value.comments = data.comment;
  postState.value.files = data.files;
}

/**
 * 1. boardService.removeConfirm(postId, postPassword) 호출하여 게시글 삭제
 * 2. 게시글 삭제 완료되면 게시글 목록 페이지로 이동
 * @returns {Promise<void>}
 */
const removeConfirm = async () => {
  try {
    await boardService.removeConfirm(postState.value.view.postId, postState.value.password)
    await boardService.goToList(router, route);
  } catch (error) {
  }
}

/* dialog 보기 */
const removePost = () => {
  dialog.value.showModal();
};

/* dialog 닫기 */
const removeCancel = () => {
  dialog.value.close();
};

/* router, route 넘겨서 게시글 목록 페이지로 이동 */
const goToList = () => {
  boardService.goToList(router, route);
};

/* router, route, postId 넘겨서 게시글 수정 페이지로 이동 */
const goToModify = () => {
  boardService.goToModify(router, route, postState.value.view.postId);
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