package framework.core.pagination;

import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public final class PaginationHelper {

    private PaginationHelper() {}

    public static <T> List<T> collectAllPages(
            Response firstResponse,
            Class<T[]> arrayType,
            Function<String, Response> nextPageFetcher
    ) {

        List<T> results = new ArrayList<>();

        Response current = firstResponse;

        while (true) {

            T[] pageItems = current.as(arrayType);

            for (T item : pageItems) {
                results.add(item);
            }

            String linkHeader = current.getHeader("Link");
            String nextUrl =
                    LinkHeaderParser.extractNextUrl(linkHeader);

            if (nextUrl == null) {
                break;
            }

            current = nextPageFetcher.apply(nextUrl);
        }

        return results;
    }
}