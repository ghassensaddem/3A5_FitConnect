<?php

namespace App\Controller;

use App\Entity\Typeactivite;
use App\Form\TypeactiviteType;
use App\Repository\TypeactiviteRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/typeactivite')]
class TypeactiviteController extends AbstractController
{
    #[Route('/', name: 'app_typeactivite_index', methods: ['GET'])]
    public function index(TypeactiviteRepository $typeactiviteRepository): Response
    {
        return $this->render('typeactivite/index.html.twig', [
            'typeactivites' => $typeactiviteRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'app_typeactivite_new', methods: ['GET', 'POST'])]
    public function new(Request $request, TypeactiviteRepository $typeactiviteRepository): Response
    {
        $typeactivite = new Typeactivite();
        $form = $this->createForm(TypeactiviteType::class, $typeactivite);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $typeactiviteRepository->add($typeactivite);
            return $this->redirectToRoute('app_typeactivite_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('typeactivite/new.html.twig', [
            'typeactivite' => $typeactivite,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_typeactivite_show', methods: ['GET'])]
    public function show(Typeactivite $typeactivite): Response
    {
        return $this->render('typeactivite/show.html.twig', [
            'typeactivite' => $typeactivite,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_typeactivite_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Typeactivite $typeactivite, TypeactiviteRepository $typeactiviteRepository): Response
    {
        $form = $this->createForm(TypeactiviteType::class, $typeactivite);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $typeactiviteRepository->add($typeactivite);
            return $this->redirectToRoute('app_typeactivite_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('typeactivite/edit.html.twig', [
            'typeactivite' => $typeactivite,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_typeactivite_delete', methods: ['POST'])]
    public function delete(Request $request, Typeactivite $typeactivite, TypeactiviteRepository $typeactiviteRepository): Response
    {
        if ($this->isCsrfTokenValid('delete'.$typeactivite->getId(), $request->request->get('_token'))) {
            $typeactiviteRepository->remove($typeactivite);
        }

        return $this->redirectToRoute('app_typeactivite_index', [], Response::HTTP_SEE_OTHER);
    }
}
