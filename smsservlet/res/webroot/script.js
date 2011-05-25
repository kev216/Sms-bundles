
function timeAfterTime(){
	var ID=window.setInterval('CheckStr()',100);
}

function CheckStr(){
	if (StrLen > 130 ){
		window.document.smsform.message.value = window.document.smsform.message.value.substring(0,130);
		StrLeft = 0;
	} else {
		StrLeft = 130 - StrLen;
	}
	document.smsform.Len.value = StrLeft;
}

var twice = false

function checkPhoneNumber(textinput){
	if (textinput.value == ""){
		alert("Veuillez taper un numéro de téléphone valide, s.v.p!");
		textinput.focus();     
		return false;    
	}

	if(textinput.value.length > 11){
		 alert("Le numéro de téléphone doit comporter 11 chiffres!");
		 textinput.focus();
		 return false;
	}

	var chkZ = 1;
	for(i=0;i<textinput.value.length;++i) {
		if(textinput.value.charAt(i) < "0"     || textinput.value.charAt(i) > "9")
		chkZ = -1;
		if(chkZ == -1)   {
			alert("Ce numéro de téléphone n'existe pas!");
			textinput.focus();
			return false;
		}
		if (twice == false){
			twice=true;	
			return true;
		} else  {	
			return false;
		}
	}
}

function clearmsg(){
	document.smsform.message.value="";
	document.smsform.charsleft.value="130";
	document.smsform.message.focus();
}

function ShowCharsLeft(smsform) {
	if(navigator.appName!="Netscape")  {
		maxLength = 130;
		if (smsform.message.value.length > maxLength) {
			smsform.message.value = smsform.message.value.substring(0,maxLength);
			charleft = 0;
		} else {
		      charleft = maxLength - smsform.message.value.length;
		}
		smsform.charsleft.value = charleft;
	}
}
