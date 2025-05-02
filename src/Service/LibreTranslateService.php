<?php
// src/Service/LibreTranslateService.php

namespace App\Service;

use Symfony\Contracts\HttpClient\HttpClientInterface;
use Psr\Log\LoggerInterface;

class LibreTranslateService
{
    private $httpClient;
    private $logger;

    public function __construct(HttpClientInterface $httpClient, LoggerInterface $logger = null)
    {
        $this->httpClient = $httpClient;
        $this->logger = $logger;
    }

    public function translate(string $text, string $target = 'en', string $source = 'auto'): string
{
    if (empty(trim($text))) {
        return $text;
    }

    try {
        // Détection automatique améliorée
        $source = $this->detectLanguage($text, $source);
        
        $response = $this->httpClient->request('GET', 'https://api.mymemory.translated.net/get', [
            'query' => [
                'q' => $text,
                'langpair' => $source . '|' . $target
            ],
            'timeout' => 5
        ]);

        $data = $response->toArray();
        return $data['responseData']['translatedText'] ?? $text;

    } catch (\Exception $e) {
        if ($this->logger) {
            $this->logger->error('Translation error: ' . $e->getMessage());
        }
        return $text;
    }
}

private function detectLanguage(string $text, string $fallback = 'auto'): string
{
    // Détection basique des langues courantes
    if (preg_match('/[\x{0600}-\x{06FF}]/u', $text)) {
        return 'ar'; // Arabe
    }
    elseif (preg_match('/\b(je|tu|il|nous|vous|ils|bonjour|merci)\b/ui', $text)) {
        return 'fr'; // Français
    }
    elseif (preg_match('/\b(the|and|hello|thanks)\b/ui', $text)) {
        return 'en'; // Anglais
    }
    
    return $fallback; // Fallback auto si aucune détection
}
}