<template>
  <form ref="modifyForm">
    <table>
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
          <input name="postPassword" type="password" v-model="postState.password">
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
import {ref, reactive, computed, onMounted} from 'vue';
import {instance} from '@/modules/axios';
import {modifyValid} from '@/modules/validation';

const router = useRouter();
const route = useRoute();

const modifyForm = ref();
const postState = reactive({
  postId: 0,
  password: '',
  categories: [],
  post: {},
  removeFiles: [],
  filesInput: [],
  postFiles: [],
  downloadLink: "http://localhost:8080/api/view/download?fileId=",
})

onMounted(() => {
  postState.postId = parseInt(sessionStorage.getItem("postId"))
  getData();
});

const filesLength = computed(() => {
  const files = postState.postFiles
  if (files === undefined) return 3;
  return (3 - files.length);
});

const getData = async () => {
  try {
    const params = {params: {postId: postState.postId}};
    const response = await instance.get("/modify", params);

    dataSetUp(response.data.object);
    postState.filesInput.length = filesLength.value;
  } catch (error) {

  }
};

const dataSetUp = (data) =>{
  postState.categories = data.categories;
  postState.post = data.post;
  postState.postFiles = data.files;
}

const modifyPost = async () => {
  try {
    const validation = modifyValid(postState.post, postState.password);
    if (!validation) return;

    const formData = new FormData(modifyForm.value);

    formData.append("postId", postState.postId);

    postState.removeFiles.forEach(item => {
      formData.append("removeFiles", item);
    });

    const response = await instance.put("/modify", formData);

    alert(response.data.message);

    sessionStorage.setItem("postId", String(postState.postId));

    await router.push({name: "View", query: route.query});

  } catch (error) {

  }
}

const fileRemove = (fileId) => {
  if (confirm("삭제?")) {
    updateFileState(fileId);
    alert("삭제완료");
  }
};

const updateFileState = (fileId) => {
  postState.postFiles = postState.postFiles.filter(file => file.fileId !== fileId);
  postState.removeFiles.push(fileId);
  postState.filesInput.length += 1;
}

const goToPost = () => {
  sessionStorage.setItem("postId", String(postState.postId));

  router.push({name: "View", query: route.query});
};
</script>