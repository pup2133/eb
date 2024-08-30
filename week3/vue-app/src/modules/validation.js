const isCategoryValid = (category) => category !== "";
const isWriterValid = (writer) => /^.{3,4}$/.test(writer);
const isTitleValid = (title) => /^.{4,99}$/.test(title);
const isContentValid = (content) => /^.{4,1999}$/.test(content);
const isPasswordValid = (password, confirmPassword) => {
    if (confirmPassword === undefined) {
        return /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{4,15}$/.test(password);
    }
    return /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{4,15}$/.test(password) && password === confirmPassword;
}

/**
 * 1. 사용자가 입력한 카테고리, 작성자, 제목, 내용 검증
 * @param post
 * @returns {boolean}
 */
const validate = (post) => {
    if (!isCategoryValid(post.categoryId)) {
        alert("카테고리를 입력해 주세요.");
        return false;
    }

    if (!isWriterValid(post.postWriter)) {
        alert("작성자는 3자 이상 5자 미만으로 입력해 주세요.");
        return false;
    }

    if (!isTitleValid(post.postTitle)) {
        alert("제목은 4자 이상 100자 미만으로 입력해 주세요.");
        return false;
    }

    if (!isContentValid(post.postContent)) {
        alert("내용은 4자 이상 2000자 미만으로로 입력해 주세요.");
        return false;
    }

    return true;
}

/**
 * 1. 수정할 때 쓰이는 검증 메서드
 * 2. 사용자가 입력한 폼 검증
 * 3. 비밀번호 검증 성공시 나머지 검증
 * @param post
 * @returns {boolean}
 */
export const modifyValid = (post) => {
    if (!isPasswordValid(post.postPassword)) {
        alert("비밀번호는 4자 이상 16자 미만이며, 문자, 숫자, 특수문자를 포함해야 합니다.");
        return false;
    }

    return validate(post);
}

/**
 * 1. 작성할 때 쓰이는 검증 메서드
 * 2. 사용자가 입력한 폼 검증
 * 3. 비밀번호 검증 성공시 나머지 검증
 * @param post
 * @returns {boolean}
 */
export const writeValid = (post) => {
    if (!isPasswordValid(post.postPassword, post.confirmPassword)) {
        alert("비밀번호는 4자 이상 16자 미만이며, 문자, 숫자, 특수문자를 포함해야 합니다.");
        return false;
    }

    return validate(post);
}