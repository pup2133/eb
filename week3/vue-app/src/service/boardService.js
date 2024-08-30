import {instance} from "@/modules/axios";

const boardService = {
    /**
     * 1. 검색 조건을 서버에 요청을 보내고, 검색 결과를 리턴
     * 2. 받은 메세지에 '검색'이 포함 될 경우 메세지를 표시하고 리턴
     * @param page
     * @param search
     * @returns {Promise<axios.AxiosResponse<any>>}:
     */
    getPostList: async (page, search) => {
        if(page!==undefined) search.value.page = page;

        const params = {
            params: search.value
        }

        const response = await instance.get("/list", params);

        if (response.data.message.includes("검색")) {
            alert(response.data.message);
            return null;
        }

        return response;
    },

    /**
     * 1. 서버에 요청을 보내고, 전체 카테고리 목록을 받아와 리턴
     * @returns {Promise<axios.AxiosResponse<any>>}
     */
    getCategories: async () => {
        return await instance.get("/write");
    },

    /**
     * 1. 작성된 폼을 서버에 요청을 보내고, 메세지를 사용자에게 알림
     * @param writeForm
     * @returns {Promise<void>}
     */
    savePost: async (writeForm) => {
        const formData = new FormData(writeForm.value);

        const response = await instance.post("/write", formData);

        alert(response.data.message);
    },

    /**
     * 1. 게시글 ID와 작성한 비밀번호를 서버에 요청을 보내고, 메세지를 사용자에게 알림
     * @param postId
     * @param postPassword
     * @returns {Promise<void>}
     */
    removeConfirm: async (postId, postPassword) => {
        const params = {params: {postId: postId, postPassword: postPassword}};

        const response = await instance.delete("/view/remove-post", params);

        alert(response.data.message);
    },

    /**
     * 1. 게시글 ID를 서버에 요청을 보내고
     * 2. ID, 제목, 작성자, 내용, 작성일자, 수정일자, 조회수, 카테고리 이름
     * 3. 데이터를 받아 리턴
     * @param postId
     * @returns {Promise<axios.AxiosResponse<any>>}
     */
    getView: async (postId) => {
        const params = {params: {postId: postId}};

        return await instance.get("/view", params);
    },

    /**
     * 1. 게시글 ID를 서버에 요청을 보내고
     * 2. 게시글 ID, 카테고리 ID, 제목, 작성자, 내용, 작성일자, 수정일자, 조회수
     * 3. 데이터를 받아 리턴
     * @param postId
     * @returns {Promise<axios.AxiosResponse<any>>}
     */
    getModify: async (postId) => {
        const params = {params: {postId: postId}};

        return await instance.get("/modify", params);
    },

    /**
     * 1. 작성 폼을 서버에 요청을 보내고, 메세지를 사용자에게 알림
     * 2. 수정 후 게시글로 이동하기 위해 세션에 postId 저장
     * @param formData
     * @returns {Promise<void>}
     */
    modifyPost: async (formData) => {
        const response = await instance.put("/modify", formData);

        alert(response.data.message);

        sessionStorage.setItem("postId", formData.get("postId"));
    },

    /**
     * 1. 작성한 댓글을 서버에 요청을 보내고
     * 2. 정상적으로 저장이 되었으면 작성자, 내용, 작성일자 데이터를 받아옴
     * 3. 받은 데이터 리턴
     * @param comment
     * @returns {Promise<axios.AxiosResponse<any>>}
     */
    saveComment: async (comment) => {
       return await instance.post("/view/save-comment", comment);
    },

    /**
     * 1.보기로 이동
     * @param router
     * @param route
     * @param postId
     */
    goToPost: (router, route, postId) => {
        sessionStorage.setItem("postId", postId);
        router.push({name: "View", query: route.query});
    },

    /**
     * 1. 등록으로 이동
     * @param router
     * @param route
     */
    goToWrite: (router, route) => {
        router.push({name: "Write", query: route.query});
    },

    /**
     * 1. 목록으로 이동
     * @param router
     * @param route
     */
    goToList: (router, route) => {
        router.push({name: "List", query: route.query});
    },

    /**
     * 1. 수정으로 이동
     * @param router
     * @param route
     * @param postId
     */
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