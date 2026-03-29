(function () {
	document.addEventListener('DOMContentLoaded', function () {
		document.querySelectorAll('form.pet-delete-form').forEach(function (form) {
			form.addEventListener('submit', function (event) {
				if (!confirm('¿Eliminar esta mascota?')) {
					event.preventDefault();
				}
			});
		});
	});
})();
