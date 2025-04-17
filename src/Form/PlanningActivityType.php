<?php

namespace App\Form;

use App\Entity\PlanningActivity;
use App\Entity\Activity;
use App\Entity\Sallesportif;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\Extension\Core\Type\IntegerType;

class PlanningActivityType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('activity', EntityType::class, [
                'label' => 'Activité',
                'class' => Activity::class,
                'choice_label' => 'nomActivity',
                'attr' => [
                    'class' => 'form-control select2'
                ],
                'placeholder' => 'Choisir une activité'
            ])
            ->add('salle', EntityType::class, [
                'label' => 'Salle de sport',
                'class' => Sallesportif::class,
                'choice_label' => 'nomSalle',
                'attr' => [
                    'class' => 'form-control select2'
                ],
                'placeholder' => 'Choisir une salle'
            ])
            ->add('capacityMax', IntegerType::class, [
                'label' => 'Capacité maximale',
                'attr' => [
                    'class' => 'form-control',
                    'min' => 1
                ]
            ])
            ->add('nombreInscription', IntegerType::class, [
                'label' => 'Nombre d\'inscriptions',
                'attr' => [
                    'class' => 'form-control',
                    'min' => 0
                ],
                'required' => false
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => PlanningActivity::class,
        ]);
    }
}