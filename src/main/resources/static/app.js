function submitData() {
    const name = document.getElementById("name").value;
    const age = document.getElementById("age").value;

    fetch("http://localhost:8080/user", {
        method : "POST",
        headers : {
        "Content-Type" : "application/json"
        },
        body : JSON.stringify({name, age}),
    })
    .then(res => res.text())
    .then(text => {
        document.getElementById("result").innerText = text + "건희";
    })
    .catch(err => {
        document.getElementById("result").innerText = "서버 오류" + err.message;
    });
}


function start() {
    document.querySelectorAll('.content-box').forEach(el => {
        el.classList.toggle('active');
    })
}

function openLayerPopup(e) {
    document.querySelector('.' + e).style.display = 'flex';
}

function closeLayerPopup(e) {
    const popup = document.querySelector('.' + e);
    
    if(!popup) {
        return;
    }

    popup.style.display = 'none';

    const firstRadio = popup.querySelector('.checkbox-area .common-radio:first-child');

    if(firstRadio) {
        firstRadio.checked = true;
    }

    const textarea = popup.querySelector('textarea')

    if(textarea) {
        textarea.value = '';
    }

}

function addList(typeData, textData, id) {
    const resultListArea = document.querySelector('.result-list-area');

    const defaultList = resultListArea.querySelector('.default');

    if(defaultList) {
        defaultList.parentElement.remove();
    }


    const newList = document.createElement('div');
    newList.classList.add('result-list');

    newList.innerHTML = `<div class="checkbox-area"><input type="checkbox" id="check_${id}" value="${id}" class="common-checkbox"><label for="check_${id}"></label></div><div class="content-area"><span class="tag">${typeData}</span><p class="content">${textData}</p><span class="author">작성일</span></div>`;

    resultListArea.appendChild(newList);

}

function submitContent() {
    const popup = document.querySelector('.layer-popup-content');

    const type = popup.querySelector('.common-radio:checked');

    const data = {
        typeData : type.value,
        textData : popup.querySelector('textarea').value
    }

    // fetch("http://localhost:8080/crudTest", {
    fetch("http://www.melloplace.com:8080/crudTest", {
        method : "POST",
        headers : {
            "Content-Type" : "application/json"
        },
        body : JSON.stringify(data)
    })
    .then(res => res.json())
    .then(result => {
        closeLayerPopup('enroll');
        // addList(data.typeData, data.textData);
        addList(data.typeData, data.textData, result.id);
    })
    .catch(err => {
        console.error("등록 실패", err);
        alert("서버 오류로 등록에 실패했습니다.");
    });
}

window.addEventListener("DOMContentLoaded", () => {
    // fetch("http://localhost:8080/crudTest")
    fetch("http://www.melloplace.com:8080/crudTest")
    .then(res => res.json())
    .then(dataList => {
        const resultListArea = document.querySelector('.result-list-area');

        resultListArea.innerHTML = "";

        dataList.forEach(item => {
            addList(item.typeData, item.textData, item.id);
        });
    })
    .catch(err => {
        console.error("데이터 로딩 실패", err);
    })
})

function deleteResultList() {

    const checkedResultList = document.querySelectorAll(".result-list input[type='checkbox']:checked");

    checkedResultList.forEach(checkbox => {
        const item = checkbox.closest('.result-list');
        const id = checkbox.value;

        if(item && id) {
            // fetch(`http://localhost:8080/crudTest/${id}`, {
            fetch(`http://www.melloplace.com:8080/crudTest/${id}`, {
                method : "DELETE"
            })
            .then(res => {
                if(res.status === 204) {
                    console.log(`id = ${id} 삭제 완료`);
                    item.remove();
                } else if (res.status === 404) {
                    alert("이미 삭제되었거나 존재하지 않는 항목입니다.");
                } else {
                    throw new Error("서버 오류");
                }
            })
            .catch(err => {
                console.error(`id=${id} DB 삭제 실패`, err);
                alert("서버 오류로 삭제에 실패했습니다.");
            })
        }
    })

    closeLayerConfirmPopup();
}

function closeLayerConfirmPopup() {
    const popup = document.querySelector('.confirm');

    popup.style.display = 'none';
}


// 검색기능

function searchContent(event) {

    event.preventDefault();

    const BASE_URL = "http://www.melloplace.com:8080";

    const field = document.getElementById("fieldSelect").value;
    const keyword = document.getElementById("keywordInput").value.trim();

    // 추후 전체 검색으로 변경할 예정
    if(!keyword) {
        alert("검색어를 입력하세요.");
        retu
    }

    fetch(`${BASE_URL}/crudTest/search?field=${field}&keyword=${encodeURIComponent(keyword)}`)
    .then(res => res.json())
    .then(dataList => {
        const area = document.querySelector('.result-list-area');

        area.innerHTML = "";

        if(dataList.length === 0) {
            area.innerHTML = "<div class='result-list default'>검색 결과가 없습니다</div>"
            return;
        }

        dataList.forEach(item => addList(item.typeData, item.textData, item.id));
    })
    .catch(err => {
        console.error("검색 실패", err);
        alert("서버 오류로 검색에 실패했습니다.");
    });

}