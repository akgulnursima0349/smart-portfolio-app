import { useState } from "react";
import axios from "../api/axios";
import toast from "react-hot-toast";

export default function AiTools() {
  const [prompt, setPrompt] = useState("");
  const [result, setResult] = useState("");
  const [loading, setLoading] = useState(false);

  const handleGenerate = async () => {
    if (!prompt.trim()) {
      toast.error("Lütfen bir prompt girin");
      return;
    }
    
    setLoading(true);
    try {
      const response = await axios.post("/ai/generate", { prompt });
      setResult(response.data.result);
      toast.success("AI açıklaması başarıyla üretildi!");
    } catch (error: any) {
      console.error("AI generation error:", error);
      const errorMessage = error.response?.data?.error || "AI servisi şu anda kullanılamıyor";
      toast.error(errorMessage);
      setResult("");
    } finally {
      setLoading(false);
    }
  };

  const handleCopyResult = () => {
    navigator.clipboard.writeText(result);
    toast.success("Sonuç panoya kopyalandı!");
  };

  const handleClearAll = () => {
    setPrompt("");
    setResult("");
    toast.success("Tüm alanlar temizlendi");
  };

  return (
    <div className="p-6 max-w-4xl mx-auto">
      <div className="mb-6">
        <h1 className="text-3xl font-bold text-gray-900 mb-2">🧠 AI Tools</h1>
        <p className="text-gray-600">AI ile proje açıklaması ve içerik üretin</p>
      </div>

      <div className="bg-white rounded-lg shadow-md p-6 mb-6">
        <div className="mb-4">
          <label htmlFor="prompt" className="block text-sm font-medium text-gray-700 mb-2">
            Proje Açıklaması İsteği
          </label>
          <textarea
            id="prompt"
            className="w-full border border-gray-300 rounded-lg p-3 focus:ring-2 focus:ring-blue-500 focus:border-transparent resize-none"
            rows={4}
            placeholder="Örnek: 'E-ticaret web sitesi için modern ve kullanıcı dostu bir açıklama yaz'"
            value={prompt}
            onChange={(e) => setPrompt(e.target.value)}
            disabled={loading}
          />
        </div>

        <div className="flex gap-3">
          <button
            onClick={handleGenerate}
            disabled={loading || !prompt.trim()}
            className="bg-blue-600 text-white px-6 py-2 rounded-lg hover:bg-blue-700 disabled:opacity-50 disabled:cursor-not-allowed flex items-center gap-2"
          >
            {loading ? (
              <>
                <div className="animate-spin rounded-full h-4 w-4 border-b-2 border-white"></div>
                Üretiliyor...
              </>
            ) : (
              <>
                ✨ Açıklama Yaz
              </>
            )}
          </button>

          <button
            onClick={handleClearAll}
            disabled={loading}
            className="bg-gray-500 text-white px-4 py-2 rounded-lg hover:bg-gray-600 disabled:opacity-50"
          >
            🗑️ Temizle
          </button>
        </div>
      </div>

      {result && (
        <div className="bg-white rounded-lg shadow-md p-6">
          <div className="flex items-center justify-between mb-4">
            <h2 className="text-xl font-semibold text-gray-900">🤖 AI Cevabı</h2>
            <button
              onClick={handleCopyResult}
              className="bg-green-600 text-white px-4 py-2 rounded-lg hover:bg-green-700 text-sm"
            >
              📋 Kopyala
            </button>
          </div>
          
          <div className="bg-gray-50 rounded-lg p-4">
            <textarea
              className="w-full border-0 bg-transparent resize-none focus:outline-none"
              rows={8}
              value={result}
              readOnly
              placeholder="AI cevabı burada görünecek..."
            />
          </div>
        </div>
      )}

      {/* Örnek Prompt'lar */}
      <div className="mt-8 bg-blue-50 rounded-lg p-6">
        <h3 className="text-lg font-semibold text-blue-900 mb-3">💡 Örnek Prompt'lar</h3>
        <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
          <button
            onClick={() => setPrompt("E-ticaret web sitesi için modern ve kullanıcı dostu bir açıklama yaz")}
            className="text-left p-3 bg-white rounded-lg border border-blue-200 hover:border-blue-400 transition-colors"
          >
            🛒 E-ticaret projesi açıklaması
          </button>
          <button
            onClick={() => setPrompt("Mobil uygulama için kısa ve etkileyici bir tanıtım yaz")}
            className="text-left p-3 bg-white rounded-lg border border-blue-200 hover:border-blue-400 transition-colors"
          >
            📱 Mobil uygulama tanıtımı
          </button>
          <button
            onClick={() => setPrompt("Portfolio web sitesi için profesyonel bir açıklama yaz")}
            className="text-left p-3 bg-white rounded-lg border border-blue-200 hover:border-blue-400 transition-colors"
          >
            💼 Portfolio sitesi açıklaması
          </button>
          <button
            onClick={() => setPrompt("Blog sitesi için detaylı ve çekici bir tanıtım yaz")}
            className="text-left p-3 bg-white rounded-lg border border-blue-200 hover:border-blue-400 transition-colors"
          >
            📝 Blog sitesi tanıtımı
          </button>
        </div>
      </div>
    </div>
  );
}
