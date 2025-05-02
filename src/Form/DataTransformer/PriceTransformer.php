<?php
namespace App\Form\DataTransformer;

use Symfony\Component\Form\DataTransformerInterface;

class PriceTransformer implements DataTransformerInterface
{
    public function transform($value)
    {
        // Transforme la valeur de la base de données vers le formulaire
        return $value === null ? '' : number_format($value, 2, '.', '');
    }

    public function reverseTransform($value)
    {
        if ($value === null || $value === '') {
            return null;
        }
    
        $value = str_replace([' ', ','], ['', '.'], $value);
        
        if (!is_numeric($value)) {
            return null;
        }
    
        return number_format((float)$value, 2, '.', '');
    }
}