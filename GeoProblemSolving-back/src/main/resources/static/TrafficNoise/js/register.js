$("#registerFrom").on("submit", function () {
    var email = $("#email").val();
    var password = $("#password").val();
    var confirmPassword = $("#confirmPassword").val();
    var userName = $("#name").val();
    var gender = $("input[name='Gender']:checked").val();
    var mobilPhone = $("#mobilPhone").val();
    var affiliation = $("#affiliation").val();

    // 如果2次密码不一致
    if (password != confirmPassword) {
        hideMessage($("#passwordCheck"));
        showMseeage($("#passwordCheck"));
        $("#confirmPassword").focus();
        return false;
    }

    var registerInfo = {
        "email": email,
        "password": password,
        "userName":userName,
        "gender":gender,
        "mobilPhone":mobilPhone,
        "affiliation":affiliation
    }
    $("#registerFrom").ajaxSubmit({
        async: true,
        type: "post",
        url: "/GeoProblemSolving/registerServlet",
        data: {
            "registerInfo": JSON.stringify(registerInfo)
        },
        dataType: "text",
        success: function (resp) {
            console.log(resp);
            var result = JSON.parse(resp);
            if (result.respCode == 1) {
                parent.location.reload();
            }
            else if(result.respCode == -1){
                hideMessage($("#emailCheck"));
                showMseeage($("#emailCheck"));
                $("#email").focus();
                return false;
            }

        },
        error: function (e) {            
            console.log(e);
        }
    })

})

$("#confirmPassword").on("input", function () {
    var password = $("#password").val();
    var confirmPassword = $("#confirmPassword").val();
    if(password){
        if(confirmPassword != password){
            showMseeage($("#passwordCheck"));
        }
    }

    if(confirmPassword == password){
        hideMessage($("#passwordCheck"));
    }
})

$("#email").on("input", function(){
    hideMessage($("#emailCheck"));
})


function showMseeage(message){
    message.show();
    message.parents(".form-group").addClass("message has-error");
}

function hideMessage(message){
    message.hide();
    message.parents(".form-group").removeClass("message has-error");
}

$(function () {
    $(window).resize(function () {
        if (document.body.clientWidth < 900) {
            window.resizeTo(899, document.body.clientWidth);
        }
    })
})
