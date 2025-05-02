<?php

namespace App\Form;

use App\Entity\Activity;
use App\Entity\Programme;
use App\Entity\Seance;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\TimeType;

class SeanceType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        ->add('horaire', TimeType::class, [
            'widget' => 'single_text',
            'input' => 'datetime',
            'required' => true,
            'html5' => true,
            'label' => 'Horaire',
        ])
        ->add('date', DateType::class, [
            'widget' => 'single_text',
            'input' => 'datetime',
            'required' => true,
            'html5' => true,
            'label' => 'Date',
        ])
            ->add('programme_id', EntityType::class, [
                'class' => Programme::class,
                'choice_label' => 'description', // Remplace 'id' par un champ plus lisible
                'label' => 'Programme',
                'attr' => ['class' => 'form-control']
            ])
            ->add('activite_id', EntityType::class, [
                'class' => Activity::class,
                'choice_label' => 'nomActivity', // Remplace 'id' par un champ plus compréhensible
                'label' => 'Activité',
                'attr' => ['class' => 'form-control']
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Seance::class,
        ]);
    }
}
