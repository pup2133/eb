const url = (u) =>{
    const params = {
        "start_date": document.getElementsByName('start_date')[0].value,
        "end_date": document.getElementsByName('end_date')[0].value,
        "category_id": document.getElementsByName('category_id')[0].value,
        "search_word": document.getElementsByName('search_word')[0].value,
        "page": document.getElementsByName('page')[0].value
    }

    return "/board/free/"+u+"?page="+params.page+"&start_date="+ params.start_date
        +"&end_date="+params.end_date+"&category_id="+params.category_id+"&search_word="+params.search_word;

    // if(params.start_date!==""&&params.end_date!==""&&params.category_id!==""&&params.search_word!==""){
    //
    // }else {
    //     return "/board/free/"+u+"?page="+params.page;
    // }
}

const isCategoryValid = (category) => category !== "";
const isWriterValid = (writer) => /^.{3,4}$/.test(writer);
const isTitleValid = (title) => /^.{4,99}$/.test(title);
const isContentValid = (content) => /^.{4,1999}$/.test(content);
const isPasswordValid = (password, confirmPassword) => {
    if(confirmPassword === undefined){
        return /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{4,15}$/.test(password);
    }
    return /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{4,15}$/.test(password) && password === confirmPassword;
}

const validate = () => {
    const category = document.getElementsByName("categoryId")[0].value;
    const writer = document.getElementsByName("postWriter")[0].value;
    const title = document.getElementsByName("postTitle")[0].value;
    const content = document.getElementsByName("postContent")[0].value;

    if (!isCategoryValid(category)) {
        alert("카테고리를 입력하세요.");
        return false;
    }

    if (!isWriterValid(writer)) {
        alert("작성자는 3자 이상 4자 이하로 입력하세요.");
        return false;
    }

    if (!isTitleValid(title)) {
        alert("제목은 4자 이상 99자 이하로 입력하세요.");
        return false;
    }

    if (!isContentValid(content)) {
        alert("내용은 4자 이상 1999자 이하로 입력하세요.");
        return false;
    }

    return true;
}

const modifyValid = () =>{
    const password = document.getElementsByName("postPassword")[0].value;

    if (!isPasswordValid(password)) {
        alert("비밀번호는 4자 이상 15자 이하이며, 문자, 숫자, 특수문자를 포함해야 합니다.");
        return false;
    }

    return validate();
}

const writeValid = () => {
    const password = document.getElementsByName("postPassword")[0].value;
    const confirmPassword = document.getElementsByName("confirmPassword")[0].value;

    if (!isPasswordValid(password, confirmPassword)) {
        alert("비밀번호는 4자 이상 15자 이하이며, 문자, 숫자, 특수문자를 포함해야 합니다.");
        return false;
    }

    return validate();
}


