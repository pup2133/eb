<template>
  <div>
    <input type="date" v-model="search.startDate">
    <input type="date" v-model="search.endDate">
    <select v-model="search.categoryId">
      <option value="" selected>전체 카테고리</option>
      <option v-for="category in postState.categories" v-bind:key="category.categoryId" v-bind:value="category.categoryId">
        {{ category.categoryName }}
      </option>
    </select>
    <input v-model="search.searchWord">
    <button type="button" @click="findPost">검색</button>
  </div>
  <h2>총 {{ postState.totalPostCount }}건</h2>
  <table>
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
    <tr v-for="post in postState.posts" v-bind:key="post.postId">
      <td>{{ post.categoryName }}
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
          <li :class="{'disabled': pages.startPage <= 1}">
            <button type="button" v-if="pages.startPage > 1" @click="getData(pages.prevRange)">&laquo;</button>
          </li>

          <!-- 페이지 번호 -->
          <li v-for="page in pageNumbers" :key="page" :class="{'active': page === pages.currentPage}">
            <button type="button" @click="getData(page)">{{ page }}</button>
          </li>

          <!-- 다음 페이지 버튼 -->
          <li :class="{'disabled': pages.endPage >= pages.totalPages}">
            <button type="button" v-if="pages.endPage < pages.totalPages" @click="getData(pages.nextRange)">&raquo;
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
import {reactive, computed, onMounted} from 'vue';
import {instance} from '@/modules/axios';

const router = useRouter();
const route = useRoute();

const postState = reactive({
  posts: [],
  totalPostCount: 0,
  categories: [],
})
const pages = reactive({
  totalPages: 0,
  startPage: 0,
  endPage: 0,
  currentPage: 0,
  nextRange: 0,
  prevRange: 0
});
const search = reactive({
  startDate: route.query.startDate || '2023-08-30',
  endDate: route.query.endDate || '2024-08-30',
  categoryId: route.query.categoryId || '',
  searchWord: route.query.searchWord || '',
  page: route.query.page || 1
});

onMounted(() => {
  getData();
});

const pageNumbers = computed(() => {
  return Array.from({length: pages.endPage - pages.startPage + 1}, (_, i) => i + pages.startPage);
});


const getData = async (page) => {
  try {
    if (page !== undefined) search.page = page;

    const params = {
      params: {
        startDate: search.startDate,
        endDate: search.endDate,
        categoryId: search.categoryId,
        searchWord: search.searchWord,
        page: search.page
      }
    }

    const response = await instance.get("/list", params);

    const data = response.data.object;
    dataSetup(data);

    await router.replace({name: "List", query: params.params});
  } catch (error) {

  }
}

const findPost = async () => {
  try {
    search.page = 1;

    const params = {
      params: {
        startDate: search.startDate,
        endDate: search.endDate,
        categoryId: search.categoryId,
        searchWord: search.searchWord,
        page: search.page
      }
    }

    const response = await instance.get("/list", params);

    if(response.data.message.includes("검색")){
      alert(response.data.message);
      return;
    }

    dataSetup(response.data.object);

    await router.replace({name: "List", query: params.params})
  } catch (error) {

  }
}

const dataSetup = (data) => {
  postState.posts = data.posts;
  postState.totalPostCount = data.totalPostCount;
  postState.categories = data.categories;

  pages.startPage = data.page.startPage;
  pages.endPage = data.page.endPage;
  pages.totalPages = data.page.totalPages;
  pages.currentPage = data.page.currentPage;
  pages.prevRange = data.page.prevRange;
  pages.nextRange = data.page.nextRange;

  search.page = pages.currentPage;
}

const goToPost = (postId) => {
  search.page = pages.currentPage;

  sessionStorage.setItem("postId", postId);

  router.push({name: "View", query: route.query});
};

const goToWrite = () => {
  search.page = pages.currentPage;

  router.push({name: "Write", query: route.query});
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