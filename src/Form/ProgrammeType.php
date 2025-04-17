<?php
// src/Form/ProgrammeType.php

namespace App\Form;

use App\Entity\Programme;
use App\Entity\Coach;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class ProgrammeType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('prix', null, [
                'label' => 'Prix',
                'attr' => ['placeholder' => 'Entrez le prix',
                'class' => 'form-control form-control-user']
                
            ])
            ->add('description', null, [
                'label' => 'Description',
                'attr' => [
                    'placeholder' => 'Entrez une description',
                    'class' => 'form-control form-control-user',
                    'style' => 'height: 50px; font-size: 18px;' // Ajuste la hauteur et la taille du texte
                ]
            ])
            
            ->add('lieu', null, [
                'label' => 'Lieu',
                'attr' => ['placeholder' => 'Lieu du programme',
                
                    'class' => 'form-control form-control-user'
                ]
            ])
            ->add('coach_id', EntityType::class, [
                'class' => Coach::class,
                'choice_label' => 'nom', // Utilise un attribut spécifique du coach (comme 'nom')
                'label' => 'Coach',
                
        'attr' => [
            'class' => 'form-control form-control-user'
        ]
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Programme::class,
        ]);
    }
}
