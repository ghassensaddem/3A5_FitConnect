import { Calendar } from '@fullcalendar/core';
import dayGridPlugin from '@fullcalendar/daygrid';

// ATTENTION : dans ton HTML, ajoute cette ligne dans ton <head> ou avant ce script :
/*
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
*/


document.addEventListener('DOMContentLoaded', function () {
  console.log('DOMContentLoaded a été déclenché.');
  var calendarEl = document.getElementById('calendar');
  console.log('Élément #calendar trouvé :', calendarEl);

  if (calendarEl) {
    var eventsData = JSON.parse(calendarEl.dataset.events);
    console.log('Données des événements :', eventsData);

    var calendar = new Calendar(calendarEl, {
      plugins: [dayGridPlugin],
      initialView: 'dayGridMonth',
      events: eventsData,
      displayEventTime: true,
      eventTimeFormat: {
        hour: '2-digit',
        minute: '2-digit',
        hour12: false
      },
      eventClick: function (info) {
        info.jsEvent.preventDefault();
        var editUrl = info.event.extendedProps.editUrl;
        var deleteUrl = info.event.extendedProps.deleteUrl;
        var csrfToken = info.event.extendedProps._token;

        // Popup choix Modifier / Supprimer
        Swal.fire({
          title: 'Que voulez-vous faire ?',
          showDenyButton: true,
          showCancelButton: true,
          confirmButtonText: 'Modifier',
          denyButtonText: 'Supprimer',
          cancelButtonText: 'Annuler',
          icon: 'question'
        }).then((result) => {
          if (result.isConfirmed) {
            // Modifier
            window.location.href = editUrl;
          } else if (result.isDenied) {
            // Confirmer la suppression
            Swal.fire({
              title: 'Êtes-vous sûr ?',
              text: "Cette action est irréversible !",
              icon: 'warning',
              showCancelButton: true,
              confirmButtonColor: '#d33',
              cancelButtonColor: '#3085d6',
              confirmButtonText: 'Oui, supprimer',
              cancelButtonText: 'Annuler'
            }).then((confirmResult) => {
              if (confirmResult.isConfirmed) {
                // Lancer la suppression
                fetch(deleteUrl, {
                  method: 'POST',
                  headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                  },
                  body: new URLSearchParams({
                    
                    _token: csrfToken
                  })
                })
                .then(response => {
                  if (response.ok) {
                    Swal.fire(
                      'Supprimé !',
                      'La séance a été supprimée avec succès.',
                      'success'
                    );
                    info.event.remove(); // Supprimer l'événement du calendrier
                  } else {
                    Swal.fire(
                      'Erreur',
                      'Une erreur est survenue lors de la suppression.',
                      'error'
                    );
                  }
                })
                .catch(error => {
                  console.error('Erreur réseau :', error);
                  Swal.fire(
                    'Erreur',
                    'Erreur réseau lors de la suppression.',
                    'error'
                  );
                });
              }
            });
          }
        });
      }
    });

    calendar.render();
    console.log('Le calendrier a été rendu.');
  } else {
    console.error("L'élément avec l'ID 'calendar' n'a pas été trouvé dans le DOM.");
  }
});
