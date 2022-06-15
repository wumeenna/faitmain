function requestPay() {
    IMP.init("imp76668016");
    IMP.request_pay({ // param
        pg: "html5_inicis",
        pay_method: "card",
        merchant_uid: "ORD20180131-0000011",
        name: "노르웨이 회전 의자",
        amount: 1000,
        buyer_email: "gildong@gmail.com",
        buyer_name: "홍길동",
        buyer_tel: "010-4242-4242",
        buyer_addr: "서울특별시 강남구 신사동",
        buyer_postcode: "01181"
    }, function (rsp) { // callback
        if (rsp.success) {
            // 결제 성공 시 로직,
            let imp_uid = rsp.imp_id;
            let merchant_uid = rsp.merchant_uid;
            let paid_amount = rsp.paid_amount;
            let apply_num = rsp.apply_num;

            let data = {
                imp_uid: imp_uid,
                merchant_uid: merchant_uid,
                paid_amount: paid_amount,
                apply_num: apply_num
            };

            $.ajax({
                url: "/order/add", data: data,
                success: function () {
                    let msg = "결제가 완료되었습니다.\n";
                    msg += "고유ID: " + imp_uid;
                    msg += "\n상점거래ID : " + merchant_uid;
                    msg += "\n결제금액 : " + paid_amount;
                    msg += "\n 카드승인번호 : " + apply_num;
                    alert(msg);
                    location.href = "index.html";
                }
            })// end ajax
        } else {
            // 결제 실패 시 로직,
            let msg = "결제에 실패하였습니다./n"
            msg = "에러내용: " + rsp.error_msg;

            alert("msg");
        }
    });
}


/* 주소입력란 버튼 숨김 & 표시 */
function showAdress(className) {
    /* 컨텐츠 동작 */
    /* 모두 숨기기 */
    $(".addressInfo_input_div").css('display', 'none');
    /* 컨텐츠 보이기 */
    $(".addressInfo_input_div_" + className).css('display', 'block');
    /* 버튼 색상 변경 */
    /* 모든 색상 동일 */
    $(".adress.btn").css('backgroundColor', '#555');
    /* 지정 색상 변경*/
    $(".adress.btn_" + className).css('backgroundColor', '#3c3838');
}

/* 포인트 입력 */
//0 이상 & 최대 포인트 수 이하
$(".order_point_input").on("propertychange change keyup paste input", function () {
    const maxPoint = parseInt('${buyer.totalPoint')
    let inputValue = parseInt($(this).val());
    if (inputValue < 0) {
        $(this).val(0);
    } else if (inputValue > maxPoint) {
        $(this).val(maxPoint);
    }

    /* 주문 조합정보란 최신화 */
    setTotalInfo();
});

/* 포인트 모두사용 취소 버튼
 * Y: 모두사용 상태 / N : 모두 취소 상태
 */
$(".order_point_input_btn").on("click", function () {
    const maxPoint = parseInt('${buyer.totalPoint')
    let state = $(this).data("state");

    if (state == 'N') {
        console.log("n동작");
        /* 모두사용 */
        //값 변경
        $(".order_point_input").val(maxPoint);
        //글 변경
        $(".order_point_input_btn_Y").css("display", "inline-block");
        $(".order_point_input_btn_N").css("display", "none");
    } else if (state == 'Y') {
        console.log("y동작");
        /* 취소 */
        //값 변경
        $(".order_point_input").val(0);
        //글 변경
        $(".order_point_input_btn_Y").css("display", "none");
        $(".order_point_input_btn_N").css("display", "inline-block");
    }

    /* 주문 조합정보란 최신화 */
    setTotalInfo();
});

/* 총 주문 정보 세팅(배송비, 총 가격, 마일리지, 물품 수, 종류) */
function setTotalInfo() {
    let totalPrice = 0;				// 총 가격
    let totalCount = 0;				// 총 갯수
    let totalKind = 0;				// 총 종류
    let totalPoint = 0;				// 총 마일리지
    let deliveryPrice = 0;			// 배송비
    let usePoint = 0;				// 사용 포인트(할인가격)
    let finalTotalPrice = 0; 		// 최종 가격(총 가격 + 배송비)

    $(".goods_table_price_td").each(function (index, element) {
        // 총 가격
        totalPrice += parseInt($(element).find(".individual_totalPrice_input").val());
        // 총 갯수
        totalCount += parseInt($(element).find(".individual_bookCount_input").val());
        // 총 종류
        totalKind += 1;
        // 총 마일리지
        totalPoint += parseInt($(element).find(".individual_totalPoint_input").val());
    });

    /* 배송비 결정 */
    if (totalPrice >= 30000) {
        deliveryPrice = 0;
    } else if (totalPrice == 0) {
        deliveryPrice = 0;
    } else {
        deliveryPrice = 3000;
    }

    finalTotalPrice = totalPrice + deliveryPrice;

    /* 사용 포인트 */
    usePoint = $(".order_point_input").val();

    finalTotalPrice = totalPrice - usePoint;

    /* 값 삽입 */
    // 총 가격
    $(".totalPrice_span").text(totalPrice.toLocaleString());
    // 총 갯수
    $(".goods_kind_div_count").text(totalCount);
    // 총 종류
    $(".goods_kind_div_kind").text(totalKind);
    // 총 마일리지
    $(".totalPoint_span").text(totalPoint.toLocaleString());
    // 배송비
    $(".delivery_price_span").text(deliveryPrice.toLocaleString());
    // 최종 가격(총 가격 + 배송비)
    $(".finalTotalPrice_span").text(finalTotalPrice.toLocaleString());
    // 할인가(사용 포인트)
    $(".usePoint_span").text(usePoint.toLocaleString());

}

$(document).ready(function () {
    /* 주문 조합정보란 최신화 */
    setTotalInfo();
});

/* selectAddress T/F */
/* 모든 selectAddress F만들기 */
$(".addressInfo_input_div").each(function (i, obj) {
    $(obj).find(".selectAddress").val("F");
});
/* 선택한 selectAdress T만들기 */
$(".addressInfo_input_div_" + className).find(".selectAddress").val("T");


/* 주문 요청 */
$(".order_btn").on("click", function () {

});


/* 이미지 삽입 */
// $(".image_wrap").each(function(i, obj){
//
//     const bobj = $(obj);
//
//     if(bobj.data("bookid")){
//         const uploadPath = bobj.data("path");
//         const uuid = bobj.data("uuid");
//         const fileName = bobj.data("filename");
//
//         const fileCallPath = encodeURIComponent(uploadPath + "/s_" + uuid + "_" + fileName);
//
//         $(this).find("img").attr('src', '/display?fileName=' + fileCallPath);
//     } else {
//         $(this).find("img").attr('src', '/resources/img/goodsNoImage.png');
//     }
//
// });


/* 다음 주소 연동 */
function execution_daum_address() {
    console.log("동작");
    new daum.Postcode({
        oncomplete: function (data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분입니다.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            var addr = ''; // 주소 변수
            var extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 사용자가 선택한 주소가 도로명 타입일때 참고항목을 조합한다.
            if (data.userSelectedType === 'R') {
                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
                if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
                    extraAddr += data.bname;
                }
                // 건물명이 있고, 공동주택일 경우 추가한다.
                if (data.buildingName !== '' && data.apartment === 'Y') {
                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
                }
                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
                if (extraAddr !== '') {
                    extraAddr = ' (' + extraAddr + ')';
                }
                // 추가해야할 코드
                // 주소변수 문자열과 참고항목 문자열 합치기
                addr += extraAddr;

            } else {
                addr += ' ';
            }

            // 제거해야할 코드
            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            $(".address1_input").val(data.zonecode);
            $(".address2_input").val(addr);
            // 커서를 상세주소 필드로 이동한다.
            $(".address3_input").attr("readonly", false);
            $(".address3_input").focus();


        }
    }).open();

}