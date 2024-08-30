<template>
  <div>
    <input type="date" v-model="search.startDate">
    <input type="date" v-model="search.endDate">
    <select v-model="search.categoryId">
      <option value="" selected>전체 카테고리</option>
      <option v-for="category in postDetails.categories" v-bind:key="category.categoryId" v-bind:value="category.categoryId">
        {{ category.categoryName }}
      </option>
    </select>
    <input v-model="search.searchWord">
    <button type="button" @click="getPostList(true,1)">검색</button>
  </div>
  <h2>총 {{ postDetails.totalPostCount }}건</h2>
  <table style="border: 1px">
    <thead>
    <tr>
      <th>카테고리</th>
      <th>제목</th>
      <th>작성자</th>
      <th>조회수</th>
      <th>작성일자</th>
      <th>수정일자</th>
    </tr>
    </thead>
    <tbody>
    <tr v-for="post in postDetails.posts" v-bind:key="post.postId">
      <td>
        {{ post.categoryName }}
        <span v-if="post.fileCount > 0">
          <svg xmlns="http://www.w3.org/2000/svg" id="Layer_1" data-name="Layer 1" viewBox="0 0 24 24" width="12" height="12">
            <path d="M7.835,16.17c-.23-.23-.446-.482-.641-.748-.325-.446-.227-1.072,.22-1.397,.446-.325,1.071-.227,1.397,.219,.129,.178,.274,.349,.437,.511,.803,.803,1.87,1.245,3.005,1.245s2.203-.442,3.005-1.245l5.5-5.5c1.657-1.657,1.657-4.354,0-6.011s-4.354-1.657-6.011,0l-1.058,1.058c-.391,.391-1.023,.391-1.414,0s-.391-1.023,0-1.414l1.058-1.058c2.437-2.438,6.402-2.438,8.839,0,2.437,2.437,2.437,6.402,0,8.839l-5.5,5.5c-1.18,1.181-2.75,1.831-4.419,1.831s-3.239-.65-4.418-1.83Zm-1.582,7.83c1.67,0,3.239-.65,4.419-1.831l1.058-1.058c.391-.39,.391-1.023,0-1.414-.39-.391-1.023-.39-1.414,0l-1.059,1.058c-.803,.803-1.87,1.245-3.005,1.245s-2.202-.442-3.005-1.245-1.245-1.87-1.245-3.005,.442-2.203,1.245-3.005l5.5-5.5c.803-.803,1.87-1.245,3.005-1.245s2.203,.442,3.005,1.245c.16,.161,.306,.332,.436,.51,.324,.447,.949,.547,1.397,.221,.447-.325,.546-.95,.221-1.397-.19-.262-.405-.513-.639-.747-1.181-1.182-2.751-1.832-4.42-1.832s-3.239,.65-4.419,1.831L1.834,13.331C.653,14.511,.003,16.081,.003,17.75c0,1.669,.65,3.239,1.831,4.419,1.18,1.181,2.749,1.831,4.419,1.831Z"/>
          </svg>
        </span>
      </td>
      <td>
        <button type="button" @click="goToPost(post.postId)">{{ post.postTitle }}</button>
      </td>
      <td>{{ post.postWriter }}</td>
      <td>{{ post.postViews }}</td>
      <td>{{ post.postCreatedDate }}</td>
      <td>{{ post.postRevisionDate }}</td>
    </tr>
    <tr>
      <td colspan="6">
        <ul class="pagination">
          <!-- 이전 페이지 버튼 -->
          <li :class="{'disabled': pageInfo.startPage <= 1}">
            <button type="button" v-if="pageInfo.startPage > 1" @click="getPostList(pageInfo.prevRange)">&laquo;</button>
          </li>

          <!-- 페이지 번호 -->
          <li v-for="page in pageNumbers" :key="page" :class="{'active': page === pageInfo.currentPage}">
            <button type="button" @click="getPostList(false,page)">{{ page }}</button>
          </li>

          <!-- 다음 페이지 버튼 -->
          <li :class="{'disabled': pageInfo.endPage >= pageInfo.totalPages}">
            <button type="button" v-if="pageInfo.endPage < pageInfo.totalPages" @click="getPostList(pageInfo.nextRange)">&raquo;
            </button>
          </li>
        </ul>
      </td>
    </tr>
    <tr>
      <td>
        <button type="button" @click="goToWrite">등록</button>
      </td>
    </tr>
    </tbody>
  </table>
</template>

<script setup>
import {useRoute, useRouter} from 'vue-router';
import {computed, onMounted, ref} from 'vue';
import boardService from "@/service/boardService";

const router = useRouter();
const route = useRoute();

const postDetails = ref({})
const pageInfo = ref({});
const search = ref({
  startDate: route.query.startDate || '2023-08-31',
  endDate: route.query.endDate || '2024-08-31',
  categoryId: route.query.categoryId || '',
  searchWord: route.query.searchWord || '',
  page: route.query.page || 1,
});

onMounted(() => {
  getPostList();
});

const pageNumbers = computed(() => {
  return Array.from(
      {length: pageInfo.value.endPage - pageInfo.value.startPage + 1},
      (_, i) => i + pageInfo.value.startPage);
});

const getPostList = async (searchStatus, page) => {
  try {
    const response = await boardService.getPostList(page, search);
    ListDataSetup(response.data.object);
    await router.replace({name: "List", query: search.value});
  } catch (error) {
  }
}

const ListDataSetup = (data) => {
  postDataSetUp(data);
  pageDataSetUp(data);
  search.value.page = pageInfo.value.currentPage;
}

const postDataSetUp = (data) => {
  postDetails.value.posts = data.posts;
  postDetails.value.totalPostCount = data.totalPostCount;
  postDetails.value.categories = data.categories;
}

const pageDataSetUp = (data) => {
  pageInfo.value.startPage = data.page.startPage;
  pageInfo.value.endPage = data.page.endPage;
  pageInfo.value.totalPages = data.page.totalPages;
  pageInfo.value.currentPage = data.page.currentPage;
  pageInfo.value.prevRange = data.page.prevRange;
  pageInfo.value.nextRange = data.page.nextRange;
}

const goToPost = (postId) => {
  boardService.goToPost(router, route, postId);
};

const goToWrite = () => {
  boardService.goToWrite(router, route);
};
</script>

<style scoped>
.pagination {
  display: flex;
  justify-content: center;
  list-style: none;
  padding: 0;
  margin: 0;
}

.pagination li {
  margin: 0 5px;
  list-style: none;
}

.pagination a {
  display: block;
  text-decoration: none;
}
</style>