var baseUrl = "http://wjwang.me/";
//var baseUrl = "http://localhost:8080/wood/";

function javaRest() {

}

javaRest.signup = function (email, password) {
    var stateContent = "";
    console.log("sign up get called");
    var form = document.createElement("form");
    form.setAttribute("method", "POST");
    form.setAttribute("action", baseUrl + "rest/ray/signup");
    var emailField = document.createElement("input");
    emailField.setAttribute("type", "hidden");
    emailField.setAttribute("name", "email");
    emailField.setAttribute("value", email);
    form.appendChild(emailField);
    var passwordField = document.createElement("input");
    passwordField.setAttribute("type", "hidden");
    passwordField.setAttribute("name", "password");
    passwordField.setAttribute("value", password);
    form.appendChild(passwordField);

    document.body.appendChild(form);
    $.ajax({
        type: "POST",
        url: baseUrl + "rest/ray/signup",
        data: jQuery(form).serialize(),
        dataType: 'text',
        success: function(data) {
            var resultArr = data.split("|");
            if (resultArr[0] == 200) {
                setCookie("calledsignup", 1, 1);
                window.location.replace("aftersignup.html");
                stateContent = "Signup success, redirecting...";
                document.getElementById("state_message")
                        .innerHTML = stateContent;
            } else {
                stateContent = "" + resultArr[1];
                document.getElementById("state_message")
                        .innerHTML = stateContent;
                document.getElementById("submit_button").disabled = false;
            }
        },
        error: function(xhr, err, status) {
            console.log(xhr + " " + err + " " + status);
            stateContent = "error code " + err;
            document.getElementById("state_message")
                    .innerHTML = stateContent + ". Please try again.";
            document.getElementById("submit_button").disabled = false;
        }
    });
    console.log("finish sign up");
}

javaRest.login = function (email, password) {
    var stateContent = "";
    console.log("log in get called");
    var form = document.createElement("form");
    form.setAttribute("method", "POST");
    form.setAttribute("action", baseUrl + "rest/ray/signin");
    var emailField = document.createElement("input");
    emailField.setAttribute("type", "hidden");
    emailField.setAttribute("name", "email");
    emailField.setAttribute("value", email);
    form.appendChild(emailField);
    var passwordField = document.createElement("input");
    passwordField.setAttribute("type", "hidden");
    passwordField.setAttribute("name", "password");
    passwordField.setAttribute("value", password);
    form.appendChild(passwordField);

    document.body.appendChild(form);
    $.ajax({
        type: "POST",
        url: baseUrl + "rest/ray/signin",
        data: jQuery(form).serialize(),
        dataType: 'text',
        success: function(data) {
            var resultArr = data.split("|");
            if (resultArr[0] == 200) {
                setCookie("useremail", email, 1);
                setCookie("hash", resultArr[1], 1);
                stateContent = "Login success, redirecting...";
                document.getElementById("state_message")
                        .innerHTML = stateContent;
                window.location.replace("welcome.html");
            } else {
                stateContent = resultArr[1];
                document.getElementById("state_message")
                        .innerHTML = stateContent;
                document.getElementById("login_button").disabled = false;
            }
        },
        error: function(xhr, err, status) {
            console.log(xhr + " " + err + " " + status);
            stateContent = "error code " + err;
            document.getElementById("state_message")
                    .innerHTML = stateContent + ". Please try again.";
            document.getElementById("login_button").disabled = false;
        }
    });
    console.log("finish sign in");
}

javaRest.getmessage = function (email, hash) {
    var stateContent = "";
    console.log("getmessage get called");

    $.ajax({
        headers: {          
             "hash": hash
        },
        url: baseUrl + "rest/ray/getmessage",
        type: "GET",
        data: { 
            "sender": email
        },
        dataType: 'json',
        success: function(result) {
            console.log("asd " + result.qwe[0].content);
        },
        error: function(xhr) {
            alert(xhr);
        }
    });
    console.log("finish getmessage");
}

javaRest.sendmessage = function (senderEmail, receiverEmail, messageContent, hash) {
    var stateContent = "";
    console.log("sendmessage get called");

    var message = {
        sender: senderEmail,
        receiver: receiverEmail,
        content: messageContent,
        timeZone: 5
    }

    $.ajax({
        headers: {          
             "hash": hash,
             "sender": email
        },
        url: baseUrl + "rest/ray/insertmessage",
        type: "PUT",
        data: message,
        dataType: 'json',
        success: function(result) {
            console.log(result);
        },
        error: function(xhr) {
            alert(xhr);
        }
    });
    console.log("finish insertmessage");
}

javaRest.afterSignUp = function () {
    var value = getCookie("calledsignup");
    if (value != 0 && value != 1) value = 0;
    setCookie("calledsignup", 0, 1);
    return value;
}

function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires +";path=/";
    console.log(cname + "=" + cvalue + "; " + expires +";path=/");
}

javaRest.is_logged_in = function () {
    return getCookie("useremail").localeCompare("") != 0;
}

function getCookie(c_name) {
    var c_value = " " + document.cookie;
    var c_start = c_value.indexOf(" " + c_name + "=");
    if (c_start == -1) {
        c_value = null;
    }
    else {
        c_start = c_value.indexOf("=", c_start) + 1;
        var c_end = c_value.indexOf(";", c_start);
        if (c_end == -1) {
            c_end = c_value.length;
        }
        c_value = unescape(c_value.substring(c_start,c_end));
    }
    return c_value;
}

function deleteCookie(c_name) {
    document.cookie = c_name + '=; expires=Thu, 01 Jan 1970 00:00:01 GMT;path=/';
}

function createRequest() {
    var result = null;
    var xmlhttp=new XMLHttpRequest();
    if (window.XMLHttpRequest) {
        // FireFox, Safari, etc.
        result = new XMLHttpRequest();
        if (typeof xmlhttp.overrideMimeType != 'undefined') {
            result.overrideMimeType('text/xml'); // Or anything else
        }
    } else if (window.ActiveXObject) {
        // MSIE
        result = new ActiveXObject("Microsoft.XMLHTTP");
    } else {
    // No known mechanism -- consider aborting the application
    }
    return result;
}

