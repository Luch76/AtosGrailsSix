<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="jquerymobile"/>

    <g:if test="${userLogin.station == null || userLogin?.station?.trim()?.length() == 0 }">
        <g:javascript>
            $(document).ready(function() {
                getLocation();
            });
        </g:javascript>
    </g:if>

</head>

<body>

<div data-role="header" >
    <div data-role="navbar" data-iconpos="left">
        <ul>
            <li><a class="home" data-icon="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
            <li><g:link controller="phoneNavigation" action="options" data-icon="gear" >Options</g:link></li>
        </ul>
    </div>
</div>

<g:render template="/templatesCommon/flashMessage" />

<g:form action="loginRun" method="post" class="ui-body ui-corner-all" onsubmit="userLoginPostSubmit()" autocomplete="off" >
    <fieldset>

        <g:hiddenField name="targetURI" value="${userLogin.targetURI}" />
        <g:hiddenField name="queryString" value="${userLogin.queryString}" />

        <div data-role="fieldcontain">
            <label for="loginType" class="select">Login Method:</label>
            <select name="loginType" id="loginType" data-mini="true" data-native-menu="true">
                <option value="STANDARD" <g:if test="${"STANDARD".equalsIgnoreCase(userLogin.loginType)}">selected="selected"</g:if>>Standard</option>
                <option value="LDAP" <g:if test="${"LDAP".equalsIgnoreCase(userLogin.loginType)}">selected="selected"</g:if>>LDAP</option>
            </select>
        </div>
        <div data-role="fieldcontain" >
            <label for="user">
                User
            </label>
            <g:textField name="user" value="${userLogin.user}" placeholder="User ID" />
        </div>

        <div data-role="fieldcontain" >
            <label for="password">
                Password
            </label>
            <input type="password" name="password" id="password" value="" />
        </div>

        <div data-role="fieldcontain" >
            <label for="station">Station</label>
            <input type="text" name="station" id="station"
                   placeholder="Station"
                   style="text-transform:uppercase;"
                   onblur="javascript:this.value=this.value.toUpperCase();"
                   value="${userLogin.station}"
            />
        </div>

        <button type="submit" name="loginRun" class="ui-btn ui-icon-forward ui-btn-icon-left">Login</button>
    </fieldset>
</g:form>
<br/>

<g:if test="${userLogin.station == null || userLogin.station.trim().length() == 0 }">
    <div id="mapholder">Getting your station...</div>
    <br/>
</g:if>

<g:javascript>
    function gotLocation(data) {
        $('#station').val(data.id);
    }
</g:javascript>


<g:javascript>
var x=document.getElementById("mapholder");

var options = {
  enableHighAccuracy: true,
  timeout: 5000,
  maximumAge: 0
};

function getLocation() {
  if (navigator.geolocation)
    {
    navigator.geolocation.getCurrentPosition(geoSuccess, error, options);
    }
  else{x.innerHTML="Geolocation is not supported by this browser.";}
  }

function error(err) {
  console.warn('ERROR(' + err.code + '): ' + err.message);
  x.innerHTML="";
};

function geoSuccess(position) {
	var img_url;
	var latlon
	;
	//x.innerHTML="Latitude: " + position.coords.latitude +  "<br>Longitude: " + position.coords.longitude;
	latlon = position.coords.latitude+","+position.coords.longitude;
	//latlon = "50" +"," + "-130";
	img_url="http://maps.googleapis.com/maps/api/staticmap?center="+latlon+"&zoom=14&size=320x300&sensor=false";
	console.log(img_url);
	console.log("About to call remote function...");
	$.getJSON(
	      '${g.createLink( controller:'phoneNavigation', action:'getGeoStation')}',
	      {latitude:position.coords.latitude, longitude:position.coords.longitude},
	      function(data) {
	      	if (data != null) {
	      		gotLocation(data);
	      	}
	      }
	    );
	console.log("Finished calling remote function.");
	x.innerHTML="";
  };

function userLoginPostSubmit() {
	var myUuid;

	myUuid = localStorage.getItem("TraxMachineCode");
	if (myUuid == null || myUuid.trim().length == 0) {
		myUuid = generateUUID();
		localStorage.setItem("TraxMachineCode", myUuid);
	}
    $("input[name='tfaMachineCode']").val(myUuid);
};

function generateUUID() {
    var d = new Date().getTime();
    var uuid = 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = (d + Math.random()*16)%16 | 0;
        d = Math.floor(d/16);
        return (c=='x' ? r : (r&0x3|0x8)).toString(16);
    });
    return uuid;
};
</g:javascript>

</body>

</html>