/**
 * Makes the delete message call to the server. Removes it from the database
 * 
 * @param e -
 *            the message id.
 * @return
 */


function initCarForm() {
	var name = $("#name"), kind = $('#kind'), description = $("#description"), maker = $("#maker"), allFields = $(
			[]).add(name).add(kind).add(description).add(maker), tips = $(".validateTips");

	function updateTips(t) {
		tips.text(t).addClass("ui-state-highlight");
		setTimeout(function() {
			tips.removeClass("ui-state-highlight", 1500);
		}, 500);
	}

	function checkLength(o, n, min, max) {
		if (o.val().length > max || o.val().length < min) {
			o.addClass("ui-state-error");
			updateTips("Length of " + n + " must be between " + min + " and "
					+ max + ".");
			return false;
		} else {
			return true;
		}
	}

	function checkRegexp(o, regexp, n) {
		if (!(regexp.test(o.val()))) {
			o.addClass("ui-state-error");
			updateTips(n);
			return false;
		} else {
			return true;
		}
	}

	$("#dialog-form")
			.dialog(
					{
						autoOpen : false,
						height : 515,
						width : 350,
						modal : true,
						buttons : {
							"Create a strain" : function() {
								var bValid = true;
								allFields.removeClass("ui-state-error");

								bValid = bValid
										&& checkLength(name, "name", 3, 16);
								bValid = bValid
										&& checkLength(maker, "maker", 3, 25);
								bValid = bValid
										&& checkLength(description,
												"description", 0, 200);

								if (bValid) {

									$(this).dialog("close");
									window.location.reload(true);

								}
							},
							Cancel : function() {
								$(this).dialog("close");
							}
						},
						close : function() {
							allFields.val("").removeClass("ui-state-error");
						}
					});
}