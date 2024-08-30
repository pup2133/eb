import {instance} from "@/modules/axios";

const boardService = {
    getPostList: async (page, search) => {
        const currentPage = search.value.currentPage;
        if (currentPage !== page) search.value.page = page;

        const params = {
            params: search.value
        }

        const response = await instance.get("/list", params);

        if (response.data.message.includes("검색")) {
            alert(response.data.message);
            return;
        }

        return response;
    },

    getCategories: async () => {
        return await instance.get("/write");
    },

    savePost: async (writeForm) => {
        const formData = new FormData(writeForm.value);

        const response = await instance.post("/write", formData);

        alert(response.data.message);
    },

    removeConfirm: async (postId, postPassword) => {
        const params = {params: {postId: postId, postPassword: postPassword}};

        const response = await instance.delete("/view/remove-post", params);

        alert(response.data.message);
    },

    getView: async (postId) => {
        const params = {params: {postId: postId}};

        return await instance.get("/view", params);
    },

    getModify: async (postId) => {
        const params = {params: {postId: postId}};

        return await instance.get("/modify", params);
    },

    modifyPost: async (formData) => {
        const response = await instance.put("/modify", formData);

        alert(response.data.message);

        sessionStorage.setItem("postId", formData.get("postId"));
    },

    goToPost: (router, route, postId) => {
        sessionStorage.setItem("postId", postId);
        router.push({name: "View", query: route.query});
    },

    goToWrite: (router, route) => {
        router.push({name: "Write", query: route.query});
    },

    goToList: (router, route) => {
        router.push({name: "List", query: route.query});
    },

    goToModify: (router, route, postId) => {
        sessionStorage.setItem("postId", postId);
        router.push({name: "Modify", query: route.query});
    }
}

export default boardService;

// const categories = ref([]);
// const state = ref({
//     postDetails: {},
//     pageInfo: {},
// })
// const postDetails = ref({
//     posts: [],
//     totalPostCount: 0,
//     categories: [],
// })
// const pageInfo = ref({
//     totalPages: 0,
//     startPage: 0,
//     endPage: 0,
//     currentPage: 0,
//     nextRange: 0,
//     prevRange: 0
// });
// const postState = ref({
//     postId: 0,
//     commentWriter: "",
//     commentContent: "",
//     password: "",
//     comments: [],
//     files: [],
//     view: {},
//     downloadLink: "http://localhost:8080/api/view/download?fileId=",
//     state: false
// })
// const modifyState = ref({
//     postId: 0,
//     password: '',
//     categories: [],
//     post: {},
//     removeFiles: [],
//     filesInput: [],
//     postFiles: [],
//     downloadLink: "http://localhost:8080/api/view/download?fileId=",
// })
// const stateDataSetUp = () => {
//     state.value.postDetails = postDetails;
//     state.value.pageInfo = pageInfo;
// }