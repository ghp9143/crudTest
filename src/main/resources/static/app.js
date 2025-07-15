// 전역변수 설정
const BASE_URL = "http://www.melloplace.com:8080";
// const BASE_URL = "http://localhost:8080";
// 수정용 input:hidden
const editIdEl = document.getElementById("editId");

let currentPage = 0;
const pageSize = 5;


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
    document.querySelector('.' + e).dataset.mode = "create";
    document.getElementById('actionType').textContent = "등록";
    document.getElementById('actionBtn').textContent = "등록";
    editIdEl.value = '';
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

    editIdEl.value = '';

}

function addList(typeData, textData, id, createdAt) {
    const resultListArea = document.querySelector('.result-list-area');

    const defaultList = resultListArea.querySelector('.default');

    if(defaultList) {
        defaultList.parentElement.remove();
    }

    const newList = document.createElement('div');
    newList.classList.add('result-list');

    const tagClass = typeData === "공지" ? "tag notice" : "tag";

    newList.innerHTML = `<div class="checkbox-area"><input type="checkbox" id="check_${id}" value="${id}" class="common-checkbox"><label for="check_${id}"></label></div><div class="content-area" onclick="openEditPopup(${id}, 'enroll')"><span class="${tagClass}">${typeData}</span><p class="content">${textData}</p><span class="author">작성일<span class="created-date">${formatRelativeTime(createdAt)}</span></span></div>`;

    resultListArea.prepend(newList);

    console.log(createdAt);

}

function submitContent() {
    const popup = document.querySelector('.layer-popup-content');

    const type = popup.querySelector('.common-radio:checked').value;
    const text = popup.querySelector('textarea').value;
    const id = editIdEl.value;

    const data = {
        typeData : type,
        textData : text
    }

    // 요청 메서드의 URL 분기 처리
    const method = id ? "PUT" : "POST";
    const url = id ? `http://www.melloplace.com:8080/crudTest/${id}` : "http://www.melloplace.com:8080/crudTest";
    // const url = id ? `http://localhost:8080/crudTest/${id}` : "http://localhost:8080/crudTest";


    fetch(url, {
        method : method,
        headers : {
            "Content-Type" : "application/json"
        },
        body : JSON.stringify(data)
    })
    .then(res => {
        if (!res.ok) throw new Error("서버 응답 오류");
        return res.json();
    })
    .then(result => {
        closeLayerPopup('enroll');
        loadPage(0);    
        // addList(data.typeData, data.textData);

        if(id) {
            const existingItem = document.querySelector(`#check_${id}`).closest('.result-list');
            existingItem.querySelector('.tag').textContent = result.typeData;
            existingItem.querySelector('.tag').className = result.typeData === "공지" ? "tag notice" : "tag";
            existingItem.querySelector('.content').textContent = result.textData;
        } else {
            addList(data.typeData, data.textData, result.id, result.createdAt);
        }
    })
    .catch(err => {
        console.error("등록/수정 실패", err);
        alert("서버 오류로 등록/저장에 실패했습니다.");
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
            addList(item.typeData, item.textData, item.id, item.createdAt);
        });
    })
    .catch(err => {
        console.error("데이터 로딩 실패", err);
    })

    loadPage(0);
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
    loadPage(currentPage);  
}

function closeLayerConfirmPopup() {
    const popup = document.querySelector('.confirm');

    popup.style.display = 'none';
}


// 검색기능

function searchContent(event) {

    event.preventDefault();

    const field = document.getElementById("fieldSelect").value;
    const keyword = document.getElementById("keywordInput").value.trim();

    // 추후 전체 검색으로 변경할 예정
    if(!keyword) {
        alert("검색어를 입력하세요.");
        return;
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

        dataList.forEach(item => addList(item.typeData, item.textData, item.id, item.createdAt));
    })
    .catch(err => {
        console.error("검색 실패", err);
        alert("서버 오류로 검색에 실패했습니다.");
    });

}


async function openEditPopup(id, e) {
    const data = await fetch(`${BASE_URL}/crudTest/${id}`).then(r => r.json());
    
    document.querySelector('.' + e).style.display = 'flex';
    document.querySelector('.' + e).dataset.mode = "edit";
    document.getElementById('actionType').textContent = "수정";
    document.getElementById('actionBtn').textContent = "저장";

    editIdEl.value = id;

    document.querySelectorAll('.common-radio').forEach( r => {
        r.checked = (r.value === data.typeData);
    });

    const popupRoot = document.querySelector('.' + e)

    console.log(data);
    popupRoot.querySelector('textarea').value = data.textData;

    
}


function formatRelativeTime(dateStr) {
    const d = new Date(dateStr);
    const diffSec = Math.floor((Date.now() - d.getTime()) / 1000);
    if (isNaN(diffSec) || diffSec < 0) return '';         // 잘못된 입력 처리

    if (diffSec < 60)               return '방금 전';
    const diffMin = Math.floor(diffSec / 60);
    if (diffMin < 60)               return `${diffMin}분 전`;
    const diffHr  = Math.floor(diffMin / 60);
    if (diffHr < 24)                return `${diffHr}시간 전`;
    const diffDay = Math.floor(diffHr / 24);
    if (diffDay < 30)               return `${diffDay}일 전`;
    const diffMon = Math.floor(diffDay / 30);
    if (diffMon < 12)               return `${diffMon}달 전`;
    const diffYr  = Math.floor(diffMon / 12);
    return `${diffYr}년 전`;
}

function drawPagination(totalPages, currentPage) {
    const paginationUl = document.querySelector('.pagination ul');
    paginationUl.innerHTML = ""; // 기존 내용 초기화

    // 이전 버튼
    const prevLi = document.createElement('li');
    prevLi.textContent = '이전';
    prevLi.classList.toggle('disabled', currentPage === 0);
    prevLi.addEventListener('click', () => {
        if (currentPage > 0) loadPage(currentPage - 1);
    });
    paginationUl.appendChild(prevLi);

    // 페이지 번호 버튼들
    for (let i = 0; i < totalPages; i++) {
        const li = document.createElement('li');
        li.textContent = i + 1;
        if (i === currentPage) li.classList.add('present');
        li.addEventListener('click', () => {
            loadPage(i);
        });
        paginationUl.appendChild(li);
    }

    // 다음 버튼
    const nextLi = document.createElement('li');
    nextLi.textContent = '다음';
    nextLi.classList.toggle('disabled', currentPage === totalPages - 1);
    nextLi.addEventListener('click', () => {
        if (currentPage < totalPages - 1) loadPage(currentPage + 1);
    });
    paginationUl.appendChild(nextLi);
}


// 🔖 추가: 페이지별 로딩
function loadPage(page) {

  currentPage = page;

  fetch(`${BASE_URL}/crudTest/paged?page=${page}&size=${pageSize}`)
    .then(res => {
        if (!res.ok) {                    // ★ 상태 코드 확인
            return res.text().then(t => {   // 4xx/5xx면 본문도 같이 보기
            throw new Error(`HTTP ${res.status}\n${t}`);
        });
      }
      return res.json();  
    })
    .then(data => {
      const area = document.querySelector('.result-list-area');
      area.innerHTML = "";
      data.content.forEach(item =>
        addList(item.typeData, item.textData, item.id, item.createdAt)
      );
      drawPagination(data.totalPages, currentPage);
    })
    .catch(err => {
        console.error("페이지 데이터 로딩 실패", err);
    });
}
