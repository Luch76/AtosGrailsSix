<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="jquerymobile"/>

    <style id="inset-tablist">
    .tablist-left {
        width: 25%;
        display: inline-block;
    }
    .tablist-content {
        width: 60%;
        display: inline-block;
        vertical-align: top;
        margin-left: 5%;
    }
    </style>



</head>

<body>


<h2>Use inset listview for tabs</h2>
<div data-role="tabs">
    <ul data-role="listview" data-inset="true" class="tablist-left">
    <li><a href="#one" data-ajax="false">one</a></li>
    <li><a href="#two" data-ajax="false">two</a></li>
    <li><a href="ajax-content-ignore.html" data-ajax="false">three</a></li>
    </ul>
    <div id="one" class="ui-body-d tablist-content">
    <h1>First tab contents</h1>
    </div>
    <ul id="two" class="tablist-content" data-role="listview" data-inset="true">
    <li><a href="#">Acura</a></li>
    <li><a href="#">Audi</a></li>
    <li><a href="#">BMW</a></li>
    <li><a href="#">Cadillac</a></li>
    <li><a href="#">Ferrari</a></li>
    </ul>
</div>


</body>

</html>