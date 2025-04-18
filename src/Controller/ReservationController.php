<?php

namespace App\Controller;

use App\Entity\Reservation;
use App\Entity\Activiteevent;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class ReservationController extends AbstractController
{
   #[Route('/reservation/increment/{id}', name: 'app_reservation_increment')]
    public function incrementOrCreate(Activiteevent $activiteevent, EntityManagerInterface $em): Response
    {
        // Cherche une réservation liée à cette activité
        $reservation = $em->getRepository(Reservation::class)->findOneBy(['event' => $activiteevent]);

        if ($reservation) {
            // Si elle existe, on incrémente simplement
            $reservation->setNbr($reservation->getNbr() + 1);
        } else {
            // Sinon, on en crée une nouvelle
            $reservation = new Reservation();
            $reservation->setEvent($activiteevent);
            $reservation->setNbr(1);
        }

        // Sauvegarde
        $em->persist($reservation);
        $em->flush();

        $this->addFlash('success', 'Réservation mise à jour pour l’activité : ' . $activiteevent->getId());

        return $this->redirectToRoute('event_list');
    }
}
