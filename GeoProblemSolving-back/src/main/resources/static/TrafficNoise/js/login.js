$("#loginForm").on("submit", function () {
    var email = $("#email").val();
    var password = $("#password").val();
    // email和password不为null、""、undefined

    var loginInfo = {
        "email": email,
        "password": password
    }
    $("#loginForm").ajaxSubmit({
        async: true,
        type: "post",
        url: "/GeoProblemSolving/loginServlet",
        data: {
            "loginInfo": JSON.stringify(loginInfo)
        },
        dataType: "text",
        success: function (resp) {
            var result = JSON.parse(resp);
            if (result.respCode == 1) {
                parent.location.reload();
            }

        },
        error: function (e) {
            console.log(e);
        }
    })

})