<!doctype html>
<html>
	<head>
		<title>jQuery SelectBox plugin</title>
		<meta http-equiv="Content-type" content="text/html; charset=utf-8" />
		<link href="css/jquery.selectbox.css" type="text/css" rel="stylesheet" />
	</head>
	<body>

		<p>View API and more demos at: <a href="http://www.bulgaria-web-developers.com/projects/javascript/selectbox/">jQuery SelectBox plugin v0.2</a></p>

		<select name="country_id" id="country_id" tabindex="1">
			<option value="">-- Select country --</option>
				<option value="1">USA</option>
				<option value="9">Canada</option>
				<option value="2">France</option>
				<option value="3">Spain</option>
				<option value="6">Bulgaria</option>
				<option value="7" disabled="disabled">Greece</option>
				<option value="8">Italy</option>
		</select>

		<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
		<script type="text/javascript" src="js/jquery.selectbox-0.2.js"></script>
		<script type="text/javascript">
		$(function () {
			$("#country_id").selectbox();
		});
		</script>
	</body>
</html>
