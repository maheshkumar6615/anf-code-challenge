var recaptchaResponse = false;
var userErrors = document.querySelector(".k1p-user-errors");
var k1pSteps = document.querySelector(".k1p-promo-step-wrapper");
var button_step1 = document.querySelector("#k1p-step1-submit");
var correctCaptcha = function(response) {
    recaptchaResponse = true;
	if(recaptchaResponse){
	k1pRegistrationButton();
	}
};

document.querySelectorAll(".k1p-date-placeholder").forEach(function(dateplaceholder){
	dateplaceholder.addEventListener("focus", function(){
		dateplaceholder.classList.remove("k1p-date-placeholder");
	});
	dateplaceholder.addEventListener("blur", function(){
		if (dateplaceholder.value == '') dateplaceholder.classList.add("k1p-date-placeholder");
	});
})

$(document).on('click', '#k1p-step1-submit', function() {
   
        if(validateK1pForm()){
			 k1pSteps.classList.add("k1p-uploading");
           getCustomerIpAddress();
     } 
return false;
});

    function  validateK1pForm () {
	// shows one error at a time. 
	// remove this string:
	//	 "error_list.length < 1 && "  
	// from the if statements to show all concurrent errors
	var error_list = [];
    var isFormValid = true;    
	userErrors.innerHTML = "";
    var mailformat = new RegExp(/^(?=(.{1,64}@.{1,255}))([!#$%&'*\-\/=?\^_`{|}~a-zA-Z0-9}]{1,64}(\.[!#$%&'*\-\/=?\^_`{|}~a-zA-Z0-9]{0,}){0,})@((\[(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}\])|([a-zA-Z0-9-]{1,63}(\.[a-zA-Z0-9-]{2,63}){1,}))$/);
	var email = $("#k1p-email").val();
    var nameformat = new RegExp(/^[^<>&\-'"()]+$/);
    var firstname = $("#k1p-firstname").val();
	var lastname = $("#k1p-lastname").val();
	//first name 
	if ( document.getElementById("k1p-firstname").reportValidity() == false ) {

        if(document.getElementById("lr-error-text").getAttribute("data-firstname-error")){
            error_list.push(document.getElementById("lr-error-text").getAttribute("data-firstname-error"));
        }else{
 			error_list.push("Please provide a first name.");
        }
	}
         //format for first name    
    if (error_list.length < 1 && !nameformat.test(firstname) && firstname != "") {
        if(document.getElementById("lr-error-text").getAttribute("data-firstname-formaterror")){
            error_list.push(document.getElementById("lr-error-text").getAttribute("data-firstname-formaterror"));
        }else{
            error_list.push("Please provide a valid First name format.");
        }
	}

      //format for last name    
    if (error_list.length < 1 && !nameformat.test(lastname) && lastname != "") {
        if(document.getElementById("lr-error-text").getAttribute("data-lastname-formaterror")){
            error_list.push(document.getElementById("lr-error-text").getAttribute("data-lastname-formaterror"));
        }else{
            error_list.push("Please provide a valid Last name format.");
        }
	}
	//last name
	if (error_list.length < 1 && document.getElementById("k1p-lastname").reportValidity() == false ) {
        if(document.getElementById("lr-error-text").getAttribute("data-lastname-error")){
            error_list.push(document.getElementById("lr-error-text").getAttribute("data-lastname-error"));
        }else{
            error_list.push("Please provide a last name.");
        }

	}
	//email stuff
	if (error_list.length < 1 && document.getElementById("k1p-email").reportValidity() == false ) {
         if(document.getElementById("lr-error-text").getAttribute("data-email-error")){
            error_list.push(document.getElementById("lr-error-text").getAttribute("data-email-error"));
        }else{
            error_list.push("Please provide a valid email.");
        }
	}
	if (error_list.length < 1 && !mailformat.test(email)) {
        if(document.getElementById("lr-error-text").getAttribute("data-email2-error")){
            error_list.push(document.getElementById("lr-error-text").getAttribute("data-email2-error"));
        }else{
            error_list.push("Please provide a valid email format.");
        }
	} 	
	if ( error_list.length < 1 && document.getElementById("k1p-email-confirm").reportValidity() == false ) {
        if(document.getElementById("lr-error-text").getAttribute("data-confemail-error")){
            error_list.push(document.getElementById("lr-error-text").getAttribute("data-confemail-error"));
        }else{
            error_list.push("Please provide a valid confirmation email.");
        }
	}
	if ( error_list.length < 1 &&  document.getElementById("k1p-email").value != document.getElementById("k1p-email-confirm").value ) {
		if(document.getElementById("lr-error-text").getAttribute("data-emailmatch-error")){
            error_list.push(document.getElementById("lr-error-text").getAttribute("data-emailmatch-error"));
        }else{
            error_list.push("Emails do not match. ");
        }
	}
	//date, must be 18yrs ago or lower
	if ( error_list.length < 1 &&  document.getElementById("k1p-date").reportValidity() == false  ) {
        if(document.getElementById("lr-error-text").getAttribute("data-date-error")){
            error_list.push(document.getElementById("lr-error-text").getAttribute("data-date-error"));
        }else{
            error_list.push("Please enter your birth date.");
        }

	}
	function subtractYears(numOfYears, date = new Date()) {
		date.setFullYear(date.getFullYear() - numOfYears);
		return date;
	}
	var age_cutoff = subtractYears(18);
	if ( error_list.length < 1 &&  new Date(document.getElementById("k1p-date").value) > age_cutoff ) {
        if(document.getElementById("lr-error-text").getAttribute("data-agecutoff-error")){
            error_list.push(document.getElementById("lr-error-text").getAttribute("data-agecutoff-error"));
        }else{
            error_list.push("Sorry, you are too young to participate. ");
        }
	}
	
	//shoesize validation
    var shoeelement = document.getElementById("k1p-shoesize");
    if(shoeelement != null){
        if ( error_list.length < 1 &&  document.getElementById("k1p-shoesize").value == false ) {
            if(document.getElementById("lr-error-text").getAttribute("data-shoesize-error")){
                error_list.push(document.getElementById("lr-error-text").getAttribute("data-shoesize-error"));
            }else{
                error_list.push("Please select a shoesize");
            }
		}
    }
	
	//terms checkbox
	if ( error_list.length < 1 &&  document.getElementById("k1p-terms").checked == false ) {
        if(document.getElementById("lr-error-text").getAttribute("data-terms-error")){
            error_list.push(document.getElementById("lr-error-text").getAttribute("data-terms-error"));
        }else{
            error_list.push("Please agree to the terms. ");
        }
	}
	
	//todo - add captcha validator
	if(error_list.length < 1 && !recaptchaResponse){
        if(document.getElementById("lr-error-text").getAttribute("data-captcha-error")){
            error_list.push(document.getElementById("lr-error-text").getAttribute("data-captcha-error"));
        }else{
             error_list.push("Select recaptcha");
        }
    }
	   
	userErrors.innerHTML = error_list.join(" <br>");
	if (error_list.length == 0) return true;
	else return false;
} 



function form_submit(customerIp) {

	var firstName = $("#k1p-firstname").val();
    sessionStorage.setItem("FirstName", firstName);
	var lastName = $("#k1p-lastname").val();
    sessionStorage.setItem("LastName", lastName);
    var fullName = firstName + " " + lastName;
	sessionStorage.setItem("FullName", fullName);
    var email = $("#k1p-email").val(); 
    sessionStorage.setItem("Email", email);
    var confirmemail = $("#k1p-email-confirm").val(); 
    var date = $("#k1p-date").val();
    sessionStorage.setItem("DOB", date);
    var tempDate = new Date(date);
    var formattedDate = tempDate.getFullYear() +"-"+ (tempDate. getMonth() + 1 ) +"-"+ tempDate. getDate();
	var termsAndConditions =  document.getElementById("k1p-terms").checked;
	var programid = $("#progid").data("programid");
	var brand = $("#progid").data("defaultsite");
	var apierror = $("#progid").data("custapimessage");
	var shoesize = $("#k1p-shoesize").val();
    var serviceCallParams = {
        firstname:firstName,
        lastname:lastName,
        email:email,
        dateOfBirth:formattedDate,
        brand: brand,
		programid:programid,
        termsAndConditions:termsAndConditions,
		apierror:apierror,
        shoesize:shoesize,
        customerIpAddress:customerIp
    };

    $.ajax({
            type: "POST",
            url: "/promotions/engage/literegistration",
            data: serviceCallParams,
            dataType: "json",
           success: function(returnedSaveData) {
				k1pSteps.classList.remove("k1p-uploading");
               if(returnedSaveData.hasOwnProperty("ErrorMsg")){
				console.log(returnedSaveData.ErrorMsg);
				userErrors.innerHTML = returnedSaveData.ErrorMsg;
                   clearStorage();
               }
               else{
				console.log("Profile id "+returnedSaveData.profileID);	
                   sessionStorage.setItem("ProfileID", returnedSaveData.profileID);
				   _satellite.track("registersuccess");
				   //setValues();
				   showStepTwo();
				   
               }
            },
                error: function (returnedSaveData){
                    k1pSteps.classList.remove("k1p-uploading");
                    console.log("error:"+returnedSaveData.responseJSON.message);
					userErrors.innerHTML = returnedSaveData.responseJSON.message;
                    clearStorage();
                }    
    });    
}

function getCustomerIpAddress(){
     $.ajax({
        url: "//api.ipify.org/?format=json",
        dataType: 'JSON',
    }).success(function(data) {
        console.log(data.ip);
         form_submit(data.ip);
    }).error(function (jqXHR, textStatus, errorThrown) {
        console.log(jqXHR);
        console.log(textStatus);
        console.log(errorThrown);
    });
}

function k1pRegistrationButton () {
	//loops through all of the required inputs in step one and removes the "button-visually-disabled" class if their value lengths are greater than 0
	let requiredK1pInputs =  document.querySelectorAll(".k1p-promo-step-1 input[required]");
	if(recaptchaResponse){
		k1pFieldsFilled();
	}
	function k1pFieldsFilled() {
		let allFieldsFilled = true;
		requiredK1pInputs.forEach(function(requiredSibling){
			if (requiredSibling.type == "checkbox" ) {
				if (requiredSibling.checked == false) allFieldsFilled = false;
			}
			else if (requiredSibling.value.length < 1) {
				allFieldsFilled = false;
			}
		})
		if (allFieldsFilled == true && recaptchaResponse) {
			button_step1.classList.remove("button-visually-disabled");
		}
	}
	requiredK1pInputs.forEach(function(reqK1pInput){
		reqK1pInput.addEventListener("change", k1pFieldsFilled)
		reqK1pInput.addEventListener("keyup",  k1pFieldsFilled)
	})
	 
}
k1pRegistrationButton();


function checkSpecificId(){

    var firstName = document.getElementById("k1p-firstname");
    var lastName = document.getElementById("k1p-lastname");

    if(firstName && lastName){
        document.getElementById("k1p-firstname").setAttribute("onkeypress", "return blockSpecificSpecialChar(event)");
        document.getElementById("k1p-lastname").setAttribute("onkeypress", "return blockSpecificSpecialChar(event)");
    }
}


function blockSpecificSpecialChar(e){
    var key;
    document.all ? key = e.keyCode : key = e.which;
    if((key!=40) && (key!=62) && (key!=60) && (key!=45) && (key!=39) && (key!=38) && (key!=34) && (key!=41)){
        return true;
        }else{
        return false;
        }
    }