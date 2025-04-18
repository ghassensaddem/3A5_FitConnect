<?php

namespace App\Controller;

use App\Entity\Event;
use App\Form\EventType;
use App\Repository\EventRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use App\Repository\ActiviteeventRepository;
use App\Repository\TypeactiviteRepository;
#[Route('/event')]
class EventController extends AbstractController
{
    #[Route('/', name: 'app_event_index', methods: ['GET'])]
    public function index(EventRepository $eventRepository): Response
    {
        $events = $eventRepository->findAll();

        if (!$events) {
            $this->addFlash('warning', 'Aucun événement trouvé.');
        }

        return $this->render('event/index.html.twig', [
            'events' => $events,
        ]);
    }

    #[Route('/new', name: 'app_event_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $event = new Event();
        $form = $this->createForm(EventType::class, $event);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $imageFile = $form->get('image')->getData();
            if ($imageFile) {
                $newFilename = uniqid().'.'.$imageFile->guessExtension();
                $imageFile->move('C:\Users\ghass\Desktop\symfonywebapp-main\public\images', $newFilename);
                $event->setImage('/images/' . $newFilename);
            }
           else
           { $event->setImage('/images/IM4.jpeg' );}
            $entityManager->persist($event);

            $entityManager->flush();

            $this->addFlash('success', 'Événement créé avec succès.');
            return $this->redirectToRoute('app_event_index');
        }

        return $this->render('event/new.html.twig', [
            'event' => $event,
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{id}', name: 'app_event_show', methods: ['GET'])]
    public function show(Event $event): Response
    {
        return $this->render('event/show.html.twig', [
            'event' => $event,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_event_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Event $event, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(EventType::class, $event);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $imageFile = $form->get('image')->getData();
            if ($imageFile) {
                $newFilename = uniqid().'.'.$imageFile->guessExtension();
                $imageFile->move('C:\Users\ghass\Desktop\symfonywebapp-main\public\images', $newFilename);
                $event->setImage('/images/' . $newFilename);
            }
           else
           { $event->setImage('/images/IM4.jpeg' );}
            $entityManager->flush();

            $this->addFlash('success', 'Événement mis à jour avec succès.');
            return $this->redirectToRoute('app_event_index');
        }

        return $this->render('event/edit.html.twig', [
            'event' => $event,
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{id}/delete', name: 'app_event_delete', methods: ['POST'])]
    public function delete(Request $request, Event $event, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete' . $event->getId(), $request->request->get('_token'))) {
            $entityManager->remove($event);
            $entityManager->flush();
            $this->addFlash('success', 'Événement supprimé avec succès.');
        }

        return $this->redirectToRoute('app_event_index');
    }
    #[Route('/event/list', name: 'event_list')]
public function list(Request $request, EventRepository $eventRepository): Response
{
    $search = $request->query->get('search');
    $sort = $request->query->get('sort', 'date'); // Trier par défaut par date

    $queryBuilder = $eventRepository->createQueryBuilder('e');

    if ($search) {
    $queryBuilder
        ->andWhere('e.lieu LIKE :search') // Retire `e.name`
        ->setParameter('search', '%' . $search . '%');
}


    if ($sort === 'prixdupass') {
        $queryBuilder->orderBy('e.prixdupass', 'ASC');
    } elseif ($sort === 'lieu') {
        $queryBuilder->orderBy('e.lieu', 'ASC');
    } else {
        $queryBuilder->orderBy('e.date', 'ASC');
    }

    $events = $queryBuilder->getQuery()->getResult();

    return $this->render('event/list.html.twig', [
        'events' => $events,
    ]);
}

#[Route('/event/{id}/details', name: 'event_details')]
public function showEventDetails(
    int $id,
    EventRepository $eventRepo,
    ActiviteeventRepository $activiteRepo,
    TypeactiviteRepository $typeactiviteRepo
): Response {
    $event = $eventRepo->find($id);
    $activities = $activiteRepo->findBy(['event' => $id]);

    // Récupération de tous les types d'activités (liés ou non à l'event, selon ton besoin)
    // Si tu veux seulement ceux liés à l'event, tu dois avoir une relation ou une logique spécifique.
    $typeactivites = $typeactiviteRepo->findAll();

    return $this->render('event/details.html.twig', [
        'event' => $event,
        'activities' => $activities,
        'typeactivites' => $typeactivites,
    ]);
}


    
}
