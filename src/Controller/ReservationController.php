<?php

namespace App\Controller;

use Twilio\Rest\Client;
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

        // Ajout du message flash
        $this->addFlash('success', 'Réservation mise à jour pour l’activité : ' . $activiteevent->getId());

        // --- Ajout de l'envoi WhatsApp ---
        try {
            // Récupérer les credentials Twilio
            $sid    = "AC6843f215e4d4a66cf94d3aebf7d5de91";
            $token  = "61f687bbef3bc267136b7ce64f25e2af";
            $twilio = new Client($sid, $token);

            // Numéros WhatsApp
            $fromNumber = "whatsapp:+14155238886"; // Ton numéro Twilio WhatsApp
            $toNumber = 'whatsapp:+21629075314'; // Le numéro du client (tu peux adapter dynamiquement si nécessaire)

            // Paramètres de message
            $messageBody = "Votre réservation pour l'activité " . 
                $activiteevent->getHoraire() . 
                " a été confirmée. Total des réservations : " . 
                $reservation->getNbr() . 
                ". Merci pour votre participation !";

            // Paramètres de contenu Twilio (contentSid et contentVariables)
            $contentSid = "HXb5b62575e6e4ff6129ad7c8efe1f983e"; // SID du contenu (message template)
            $contentVariables = '{"1":"12/1","2":"3pm"}'; // Variables dynamiques du message

            // Envoi via WhatsApp
            $message = $twilio->messages->create(
                $toNumber, 
                [
                    "from" => $fromNumber,
                    "contentSid" => $contentSid, // Ajout du contentSid
                    "contentVariables" => $contentVariables, // Ajout des variables
                    "body" => $messageBody // Corps du message
                ]
            );

            // Affiche l'ID du message pour débogage (si nécessaire)
            print($message->sid);

        } catch (\Exception $e) {
            // Gestion des erreurs Twilio
            $this->addFlash('error', 'Erreur lors de l\'envoi du message WhatsApp : ' . $e->getMessage());
        }

        // Redirection après succès
        return $this->redirectToRoute('event_list'); // Redirige vers la page d'index des événements
    }
}
