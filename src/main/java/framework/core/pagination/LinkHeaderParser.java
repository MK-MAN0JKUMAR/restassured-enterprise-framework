package framework.core.pagination;

public final class LinkHeaderParser {

    private LinkHeaderParser() {}

    public static String extractNextUrl(String linkHeader) {

        if (linkHeader == null || linkHeader.isBlank()) {
            return null;
        }

        String[] parts = linkHeader.split(",");

        for (String part : parts) {

            if (part.contains("rel=\"next\"")) {

                int start = part.indexOf("<") + 1;
                int end = part.indexOf(">");

                if (start > 0 && end > start) {
                    return part.substring(start, end);
                }
            }
        }

        return null;
    }
}