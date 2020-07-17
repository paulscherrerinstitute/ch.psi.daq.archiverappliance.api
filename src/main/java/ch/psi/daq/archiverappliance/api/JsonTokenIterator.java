package ch.psi.daq.archiverappliance.api;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class JsonTokenIterator implements Iterator<JsonToken> {
   private static final Logger LOGGER = LoggerFactory.getLogger(JsonTokenIterator.class);
   private final JsonParser jParser;
   private final JsonToken endToken;

   public JsonTokenIterator(final JsonParser jParser, final JsonToken endToken) {
      this.jParser = jParser;
      this.endToken = endToken;

      try {
         jParser.nextToken();
      } catch (final Exception e) {
         LOGGER.warn("Could not forward to first element.", e);
      }
   }

   @Override
   public boolean hasNext() {
      // check if not yet consumed
      if (jParser.currentToken() == null) {
         try {
            return jParser.nextToken() != endToken;
         } catch (final Exception e) {
            LOGGER.warn("Could not forward element.", e);
            return false;
         }
      } else {
         return jParser.currentToken() != endToken;
      }
   }

   @Override
   public JsonToken next() {
      return jParser.currentToken();
   }
}
