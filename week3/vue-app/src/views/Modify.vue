<template>
  <form ref="modifyForm">
    <table style="border: 1px">
      <tr>
        <td>카테고리*</td>
        <td>
          <select name="categoryId">
            <option v-for="category in postState.categories"
                    v-bind:key="category.categoryId"
                    v-bind:value="category.categoryId"
                    v-bind:selected="category.categoryId === postState.post.categoryId">
              {{ category.categoryName }}
            </option>
          </select>
        </td>
      </tr>
      <tr>
        <td>등록 일시</td>
        <td>{{ postState.post.postCreatedDate }}</td>
      </tr>
      <tr>
        <td>수정 일시</td>
        <td>{{ postState.post.postRevisionDate }}</td>
      </tr>
      <tr>
        <td>조회수</td>
        <td>{{ postState.post.postViews }}</td>
      </tr>
      <tr>
        <td>작성자*</td>
        <td><input name="postWriter" type="text" v-model="postState.post.postWriter"></td>
      </tr>
      <tr>
        <td>비밀번호</td>
        <td>
          <input name="postPassword" type="password" v-model="postState.post.postPassword" placeholder="비밀번호를 입력해 주세요">
        </td>
      </tr>
      <tr>
        <td>제목*</td>
        <td>
          <input name="postTitle" type="text" v-model="postState.post.postTitle">
        </td>
      </tr>
      <tr>
        <td>내용</td>
        <td>
          <textarea name="postContent" v-model="postState.post.postContent"></textarea>
        </td>
      </tr>
      <tr>
        <td>파일첨부</td>
        <td id="files">
          <div v-for="file in postState.postFiles">
            <svg xmlns="http://www.w3.org/2000/svg" id="Layer_1" data-name="Layer 1" viewBox="0 0 24 24" width="12" height="12">
              <path d="M7.835,16.17c-.23-.23-.446-.482-.641-.748-.325-.446-.227-1.072,.22-1.397,.446-.325,1.071-.227,1.397,.219,.129,.178,.274,.349,.437,.511,.803,.803,1.87,1.245,3.005,1.245s2.203-.442,3.005-1.245l5.5-5.5c1.657-1.657,1.657-4.354,0-6.011s-4.354-1.657-6.011,0l-1.058,1.058c-.391,.391-1.023,.391-1.414,0s-.391-1.023,0-1.414l1.058-1.058c2.437-2.438,6.402-2.438,8.839,0,2.437,2.437,2.437,6.402,0,8.839l-5.5,5.5c-1.18,1.181-2.75,1.831-4.419,1.831s-3.239-.65-4.418-1.83Zm-1.582,7.83c1.67,0,3.239-.65,4.419-1.831l1.058-1.058c.391-.39,.391-1.023,0-1.414-.39-.391-1.023-.39-1.414,0l-1.059,1.058c-.803,.803-1.87,1.245-3.005,1.245s-2.202-.442-3.005-1.245-1.245-1.87-1.245-3.005,.442-2.203,1.245-3.005l5.5-5.5c.803-.803,1.87-1.245,3.005-1.245s2.203,.442,3.005,1.245c.16,.161,.306,.332,.436,.51,.324,.447,.949,.547,1.397,.221,.447-.325,.546-.95,.221-1.397-.19-.262-.405-.513-.639-.747-1.181-1.182-2.751-1.832-4.42-1.832s-3.239,.65-4.419,1.831L1.834,13.331C.653,14.511,.003,16.081,.003,17.75c0,1.669,.65,3.239,1.831,4.419,1.18,1.181,2.749,1.831,4.419,1.831Z"/>
            </svg>
            {{ file.fileName }}
            <a :href="postState.downloadLink+file.fileId">다운로드</a>
            <button type="button" @click="fileRemove(file.fileId)">X</button>
          </div>
          <div id="fileUpload">
            <input v-for="n in postState.filesInput" v-bind:key="n" name="file" type="file">
          </div>
        </td>
      </tr>
      <tr>
        <td>
          <button type="button" @click="goToPost">취소</button>
        </td>
        <td>
          <button type="button" @click="modifyPost">저장</button>
        </td>
      </tr>
    </table>
  </form>
</template>

<script setup>
import {useRoute, useRouter} from 'vue-router';
import {ref, computed, onMounted} from 'vue';
import {modifyValid} from '@/modules/validation';
import boardService from "@/service/boardService";

const router = useRouter();
const route = useRoute();

/* <form> */
const modifyForm = ref();

/**
 * 1. post: 게시글 정보
 * 2. categories: 전체 카테고리 목록
 * 3. fileInput: <input type="file"> 요소의 목록을 조절하기 위해 사용
 * 4. postFiles: 게시글에 업로드된 파일 목록
 * @type {Ref<UnwrapRef<{post: {}, downloadLink: string, postFiles: *[], categories: *[], filesInput: *[]}>>}
 */
const postState = ref({
  post: {},
  categories: [],
  filesInput: [],
  postFiles: [],
  downloadLink: "http://localhost:8080/api/view/download?fileId=",
});

/* 컴포넌트 마운트 된 후 실행 */
onMounted(() => {
  postState.value.post.postId = parseInt(sessionStorage.getItem("postId"))
  getModify();
});

/**
 *  1. filesInput의 길이를 계산하기 위해 사용
 *  2. 업로드 파일이 3개를 넘지 않게 하기 위해 (postFile.Length + filesInput)의 길이가 3이 넘지 않게 설정
 *  3. 예를 들어  게시글에 업로드 된 파일이 2개라면 fileInput은 1
 */
const filesLength = computed(() => {
  const files = postState.value.postFiles
  if (files === undefined) return 3;
  return (3 - files.length);
});

/**
 * 1. onMounted()로 실행됨
 * 2. boardService.getModify(postId) 호출하여 받아온 데이터로
 * 3. postState의 상태를 설정함
 * @returns {Promise<void>}
 */
const getModify = async () => {
  try {
    const response = await boardService.getModify(postState.value.post.postId);
    modifyDataSetUp(response.data.object);
  } catch (error) {
  }
};

/**
 * 1. 서버에서 받아온 데이터로 postState의 상태를 설정
 * @param data
 */
const modifyDataSetUp = (data) => {
  postState.value.categories = data.categories;
  postState.value.post = data.post;
  postState.value.postFiles = data.files;
  postState.value.postId = postState.value.post.postId;
  postState.value.post.removeFiles = [];
  postState.value.post.postPassword = "";

  postState.value.filesInput.length = filesLength.value;
}

/**
 * 1. 검증 실행
 * 2. 성공하면 formData 생성 후 폼에 postId와 removeFiles 값 추가
 * 3. boardService.modifyPost(formData)를 호출해서 게시물 수정
 * 4. 수정이 완료되면, 게시글 상세 페이지로 이동
 * @returns {Promise<void>}
 */
const modifyPost = async () => {
  try {
    const validation = modifyValid(postState.value.post);
    if (!validation) return;

    const formData = new FormData(modifyForm.value);

    formData.append("postId", postState.value.postId);

    postState.value.post.removeFiles.forEach(item => {
      formData.append("removeFiles", item);
    });

    await boardService.modifyPost(formData);

    await router.push({name: "View", query: route.query});

  } catch (error) {
  }
}

/**
 * 1. 파일 삭제 버튼을 누르면
 * 2. updateFileState(fileId)를 넘겨 상태를 설정함
 * @param fileId
 */
const fileRemove = (fileId) => {
  if (confirm("해당 파일을 삭제하시겠습니까?")) {
    updateFileState(fileId);
    alert("파일이 삭제되었습니다.");
  }
};

/**
 * 1. postFiles, removeFiles, filesInput 상태 변경
 * @param fileId
 */
const updateFileState = (fileId) => {
  postState.value.postFiles = postState.value.postFiles.filter(file => file.fileId !== fileId);
  postState.value.post.removeFiles.push(fileId);
  postState.value.filesInput.length += 1;
}

/* router, route, postId 넘겨서 게시글 상세 페이지로 이동 */
const goToPost = () => {
  boardService.goToPost(router, route, postState.value.post.postId)
};
</script>